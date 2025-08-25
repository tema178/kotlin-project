package repo

import models.Lock
import models.Resource
import models.ResourceId

data class DbIdRequest(
    val id: ResourceId,
    val lock: Lock = Lock.NONE,
) {
    constructor(ad: Resource): this(ad.id, ad.lock)
}
