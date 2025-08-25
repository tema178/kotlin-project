package repo

import Context
import ICorChainDsl
import models.State
import util.fail
import worker

fun ICorChainDsl<Context>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == State.RUNNING && validated.lock != adRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(adRepoRead, validated.lock).errors)
    }
}
