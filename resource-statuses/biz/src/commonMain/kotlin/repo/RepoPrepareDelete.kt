package repo

import Context
import ICorChainDsl
import models.State
import worker

fun ICorChainDsl<Context>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Готовим данные к удалению из БД
    """.trimIndent()
    on { state == State.RUNNING }
    handle {
        adRepoPrepare = validated.copy()
    }
}
