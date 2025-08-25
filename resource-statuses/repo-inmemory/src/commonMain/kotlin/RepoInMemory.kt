import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import models.*
import repo.*
import repo.exceptions.RepoEmptyLockException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class RepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : RepoBase(), IRepo, IRepoInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, Entity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(resources: Collection<Resource>) = resources.map { ad ->
        val entity = Entity(ad)
        require(entity.id != null)
        cache.put(entity.id, entity)
        ad
    }

    override suspend fun create(rq: DbRequest): IDbResourceResponse = tryMethod {
        val key = randomUuid()
        val ad = rq.resource.copy(id = ResourceId(key), lock = Lock(randomUuid()))
        val entity = Entity(ad)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbResponseOk(ad)
    }

    override suspend fun read(rq: DbIdRequest): IDbResourceResponse = tryMethod {
        val key = rq.id.takeIf { it != ResourceId.NONE }?.asString() ?: return@tryMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun update(rq: DbRequest): IDbResourceResponse = tryMethod {
        val rqRes = rq.resource
        val id = rqRes.id.takeIf { it != ResourceId.NONE } ?: return@tryMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqRes.lock.takeIf { it != Lock.NONE } ?: return@tryMethod errorEmptyLock(id)

        mutex.withLock {
            val old = cache.get(key)?.toInternal()
            when {
                old == null -> errorNotFound(id)
                old.lock == Lock.NONE -> errorDb(RepoEmptyLockException(id))
                old.lock != oldLock -> errorRepoConcurrency(old, oldLock)
                else -> {
                    val new = rqRes.copy(lock = Lock(randomUuid()))
                    val entity = Entity(new)
                    cache.put(key, entity)
                    DbResponseOk(new)
                }
            }
        }
    }


    override suspend fun delete(rq: DbIdRequest): IDbResourceResponse = tryMethod {
        val id = rq.id.takeIf { it != ResourceId.NONE } ?: return@tryMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != Lock.NONE } ?: return@tryMethod errorEmptyLock(id)


        mutex.withLock {
            val old = cache.get(key)?.toInternal()
            when {
                old == null -> errorNotFound(id)
                old.lock == Lock.NONE -> errorDb(RepoEmptyLockException(id))
                old.lock != oldLock -> errorRepoConcurrency(old, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbResponseOk(old)
                }
            }
        }
    }

    override suspend fun search(rq: DbFilterRequest): IDbResponses = tryResourcesMethod {
        val result: List<Resource> = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != UserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.type.takeIf { it != ResourceType.NONE}?.let {
                    it.asString() == entry.value.type.toString()
                } ?: true
            }
            .filter { entry ->
                rq.status.takeIf { it != ResourceStatus.NONE }?.let {
                    it.asString() == entry.value.status.toString()
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbResponsesOk(result)
    }
}
