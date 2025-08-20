package repo

import Context
import ICorChainDsl
import models.State
import util.fail
import worker

fun ICorChainDsl<Context>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        val request = DbRequest(adRepoPrepare)
        when(val result = adRepo.update(request)) {
            is DbResponseOk -> adRepoDone = result.data
            is DbResponseErr -> fail(result.errors)
            is DbResponseErrWithData -> {
                fail(result.errors)
                adRepoDone = result.data
            }
        }
    }
}
