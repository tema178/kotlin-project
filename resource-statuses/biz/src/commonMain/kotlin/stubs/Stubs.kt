package stubs

import Context
import ICorChainDsl
import chain
import models.State
import models.WorkMode

fun ICorChainDsl<Context>.stubs(title: String, block: ICorChainDsl<Context>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == WorkMode.STUB && state == State.RUNNING }
}
