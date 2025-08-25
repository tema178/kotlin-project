import com.benasher44.uuid.uuid4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import repo.*
import util.asResourceError

class RepoSql(
    properties: SqlProperties,
    private val randomUuid: () -> String = { uuid4().toString() }
) : IRepo, IRepoInitializable {
    private val table = Table("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    fun clear(): Unit = transaction(conn) {
        table.deleteAll()
    }

    private fun saveObj(ad: Resource): Resource = transaction(conn) {
        val res = table
            .insert {
                to(it, ad, randomUuid)
            }
            .resultedValues
            ?.map { table.from(it) }
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(crossinline block: () -> T, crossinline handle: (Exception) -> T): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbResourceResponse): IDbResourceResponse =
        transactionWrapper(block) { DbResponseErr(it.asResourceError()) }

    override fun save(resources: Collection<Resource>): Collection<Resource> = resources.map { saveObj(it) }
    override suspend fun create(rq: DbRequest): IDbResourceResponse = transactionWrapper {
        DbResponseOk(saveObj(rq.resource))
    }

    private fun read(id: ResourceId): IDbResourceResponse {
        val res = table.selectAll().where {
            table.id eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbResponseOk(table.from(res))
    }

    override suspend fun read(rq: DbIdRequest): IDbResourceResponse = transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: ResourceId,
        lock: Lock,
        block: (Resource) -> IDbResourceResponse
    ): IDbResourceResponse =
        transactionWrapper {
            if (id == ResourceId.NONE) return@transactionWrapper errorEmptyId

            val current = table.selectAll().where { table.id eq id.asString() }
                .singleOrNull()
                ?.let { table.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }


    override suspend fun update(rq: DbRequest): IDbResourceResponse = update(rq.resource.id, rq.resource.lock) {
        table.update({ table.id eq rq.resource.id.asString() }) {
            to(it, rq.resource.copy(lock = Lock(randomUuid())), randomUuid)
        }
        read(rq.resource.id)
    }

    override suspend fun delete(rq: DbIdRequest): IDbResourceResponse = update(rq.id, rq.lock) {
        table.deleteWhere { id eq rq.id.asString() }
        DbResponseOk(it)
    }

    override suspend fun search(rq: DbFilterRequest): IDbResponses =
        transactionWrapper({
            val res = table.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.ownerId != UserId.NONE) {
                        add(table.updatedBy eq rq.ownerId.asString())
                    }
                    if (rq.type != ResourceType.NONE) {
                        add(table.type eq rq.type.asString())
                    }
                    if (rq.status != ResourceStatus.NONE) {
                        add(table.status eq rq.status.asString())
                    }
                    if (rq.name.isNotBlank()) {
                        add(table.name eq rq.name)
                    }
                }.reduce { a, b -> a and b }
            }
            DbResponsesOk(data = res.map { table.from(it) })
        }, {
            DbResourcesResponseErr(it.asResourceError())
        })
}
