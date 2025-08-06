package repo

import Context
import ICorChainDsl
import models.State
import util.fail
import worker

fun ICorChainDsl<Context>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск объявлений в БД по фильтру"
    on { state == State.RUNNING }
    handle {
        val request = DbFilterRequest(
            type = filterValidated.type,
            status = filterValidated.status,
            ownerId = filterValidated.ownerId,
        )
        when(val result = adRepo.search(request)) {
            is DbResponsesOk -> adsRepoDone = result.data.toMutableList()
            is DbAdsResponseErr -> fail(result.errors)
        }
    }
}
