package repo

import Context
import ICorChainDsl
import models.State
import util.fail
import worker

fun ICorChainDsl<Context>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    on { state == State.RUNNING }
    handle {
        val request = DbIdRequest(validated)
        when(val result = adRepo.read(request)) {
            is DbResponseOk -> adRepoRead = result.data
            is DbResponseErr -> fail(result.errors)
            is DbResponseErrWithData -> {
                fail(result.errors)
                adRepoRead = result.data
            }
        }
    }
}
