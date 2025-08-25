package repo

import Context
import ICorChainDsl
import models.State
import worker

fun ICorChainDsl<Context>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == State.RUNNING }
    handle {
        adRepoPrepare = adRepoRead.copy().apply {
            name = validated.name
            type = validated.type
            status = validated.status
            lock = validated.lock
        }
    }
}
