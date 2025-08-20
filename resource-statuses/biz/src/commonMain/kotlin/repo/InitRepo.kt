package repo

import Context
import ICorChainDsl
import exceptions.DbNotConfiguredException
import models.WorkMode
import util.errorSystem
import util.fail
import worker

fun ICorChainDsl<Context>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        adRepo = when {
            workMode == WorkMode.TEST -> corSettings.repoTest
            workMode == WorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != WorkMode.STUB && adRepo == IRepo.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = DbNotConfiguredException(workMode)
            )
        )
    }
}
