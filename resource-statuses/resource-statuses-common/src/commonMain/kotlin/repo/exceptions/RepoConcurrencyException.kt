package repo.exceptions

import models.Lock
import models.ResourceId

class RepoConcurrencyException(id: ResourceId, expectedLock: Lock, actualLock: Lock?): RepoResourceException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
