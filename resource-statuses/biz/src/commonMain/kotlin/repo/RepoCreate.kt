package repo

import Context
import ICorChainDsl
import models.State
import util.fail
import worker


fun ICorChainDsl<Context>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление объявления в БД"
    on { state == State.RUNNING }
    handle {
        val request = DbRequest(adRepoPrepare)
        when(val result = adRepo.create(request)) {
            is DbResponseOk -> adRepoDone = result.data
            is DbResponseErr -> fail(result.errors)
            is DbResponseErrWithData -> fail(result.errors)
        }
    }
}
