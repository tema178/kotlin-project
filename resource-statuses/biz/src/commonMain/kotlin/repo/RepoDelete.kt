package repo

import Context
import ICorChainDsl
import models.State
import util.fail
import worker

fun ICorChainDsl<Context>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == State.RUNNING }
    handle {
        val request = DbIdRequest(adRepoPrepare)
        when(val result = adRepo.delete(request)) {
            is DbResponseOk -> adRepoDone = result.data
            is DbResponseErr -> {
                fail(result.errors)
                adRepoDone = adRepoRead
            }
            is DbAdResponseErrWithData -> {
                fail(result.errors)
                adRepoDone = result.data
            }
        }
    }
}
