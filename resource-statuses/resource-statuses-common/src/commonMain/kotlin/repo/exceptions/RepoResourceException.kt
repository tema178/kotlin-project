package repo.exceptions

import models.ResourceId

open class RepoResourceException(
    @Suppress("unused")
    val adId: ResourceId,
    msg: String,
): RepoException(msg)
