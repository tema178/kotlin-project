import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import models.*

class Table(tableName: String) : Table(tableName) {
    val id = text(SqlFields.ID)
    val type = text(SqlFields.TYPE)
    val status = text(SqlFields.STATUS)
    val updatedAt = long(SqlFields.UPDATED_AT)
    val updatedBy = text(SqlFields.UPDATED_BY_ID)
    val lock = text(SqlFields.LOCK)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = Resource(
        id = ResourceId(res[id].toString()),
        type = ResourceType(res[type]),
        status = ResourceStatus(res[status]),
        updatedBy = UserId(res[updatedBy].toString()),
        updatedAt = Instant.fromEpochMilliseconds(res[updatedAt]),
        lock = Lock(res[lock]),
        )

    fun to(it: UpdateBuilder<*>, res: Resource, randomUuid: () -> String) {
        it[id] = res.id.takeIf { it != ResourceId.NONE }?.asString() ?: randomUuid()
        it[type] = res.type.asString()
        it[status] = res.status.asString()
        it[updatedAt] = res.updatedAt.toEpochMilliseconds()
        it[updatedBy] = res.updatedBy.asString()
        it[lock] = res.lock.takeIf { it != Lock.NONE }?.asString() ?: randomUuid()
    }

}

