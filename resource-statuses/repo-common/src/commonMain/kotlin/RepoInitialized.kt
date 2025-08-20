import models.Resource

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class RepoInitialized(
    val repo: IRepoInitializable,
    initObjects: Collection<Resource> = emptyList(),
) : IRepoInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<Resource> = save(initObjects).toList()
}
