import models.*
import kotlinx.datetime.Instant
import repo.IRepo
import stubs.Stubs

data class Context(
    var command: Command = Command.NONE,
    var state: State = State.NONE,
    val errors: MutableList<ResourceError> = mutableListOf(),

    var corSettings: CorSettings = CorSettings(),
    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: Stubs = Stubs.NONE,

    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var request: Resource = Resource(),
    var filterRequest: Filter = Filter(),

    var validating: Resource = Resource(),
    var validated: Resource = Resource(),

    var filterValidating: Filter = Filter(),
    var filterValidated: Filter = Filter(),

    var adRepo: IRepo = IRepo.NONE,
    var adRepoRead: Resource = Resource(), // То, что прочитали из репозитория
    var adRepoPrepare: Resource = Resource(), // То, что готовим для сохранения в БД
    var adRepoDone: Resource = Resource(),  // Результат, полученный из БД
    var adsRepoDone: MutableList<Resource> = mutableListOf(),


    var resource: Resource = Resource(),
    var resources: MutableList<Resource> = mutableListOf(),
    )
