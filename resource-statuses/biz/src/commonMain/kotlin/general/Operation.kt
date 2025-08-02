package general

import Context
import ICorChainDsl
import chain
import models.Command
import models.State

fun ICorChainDsl<Context>.operation(
    title: String,
    command: Command,
    block: ICorChainDsl<Context>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == State.RUNNING }
}
