package repo

import models.Resource
import models.ResourceId

data class DbIdRequest(
    val id: ResourceId,
) {
    constructor(ad: Resource): this(ad.id)
}
