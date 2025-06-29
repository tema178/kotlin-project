import models.State

@Suppress("unused", "RedundantSuspendModifier")
class StatusProcessor(val corSettings: CorSettings) {
    suspend fun exec(ctx: Context) {
        ctx.resource = ResourceStub.get()
        ctx.state = State.RUNNING
    }
}
