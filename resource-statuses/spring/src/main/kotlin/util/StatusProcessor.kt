package io.tema.app.spring.util

import Context
import ResourceStub
import models.State

@Suppress("unused", "RedundantSuspendModifier")
class StatusProcessor {
    suspend fun exec(ctx: Context) {
        ctx.resource = ResourceStub.get()
        ctx.state = State.RUNNING
    }
}
