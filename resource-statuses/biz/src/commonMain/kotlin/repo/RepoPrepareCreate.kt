package repo

import Context
import ICorChainDsl
import models.State
import worker

fun ICorChainDsl<Context>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == State.RUNNING }
    handle {
        adRepoPrepare = validated.copy()
    }
}
