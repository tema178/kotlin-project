import repo.IRepo

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val repoStub: IRepo = IRepo.NONE,
    val repoTest: IRepo = IRepo.NONE,
    val repoProd: IRepo = IRepo.NONE,
) {
    companion object {
        val NONE = CorSettings()
    }
}
