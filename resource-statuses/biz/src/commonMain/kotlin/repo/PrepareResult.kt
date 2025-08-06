package repo

import Context
import ICorChainDsl
import models.State
import models.WorkMode
import worker

fun ICorChainDsl<Context>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != WorkMode.STUB }
    handle {
        resource = adRepoDone
        resources = adsRepoDone
        state = when (val st = state) {
            State.RUNNING -> State.FINISHING
            else -> st
        }
    }
}
