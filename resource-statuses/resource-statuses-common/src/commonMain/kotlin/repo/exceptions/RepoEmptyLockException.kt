package repo.exceptions

import models.ResourceId

class RepoEmptyLockException(id: ResourceId): RepoResourceException(
    id,
    "Lock is empty in DB"
)
