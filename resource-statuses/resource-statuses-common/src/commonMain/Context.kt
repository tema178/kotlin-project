import models.*
import kotlinx.datetime.Instant
import stubs.Stubs

data class Context(
    var command: Command = Command.NONE,
    var state: State = State.NONE,
    val errors: MutableList<ResourceError> = mutableListOf(),

    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: Stubs = Stubs.NONE,

    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var request: Resource = Resource(),
    var filterRequest: Filter = Filter(),

    var resource: Resource = Resource(),
    var resources: MutableList<Resource> = mutableListOf(),

    )
