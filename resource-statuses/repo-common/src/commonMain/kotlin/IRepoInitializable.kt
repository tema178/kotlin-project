import models.Resource
import repo.IRepo

interface IRepoInitializable: IRepo {
    fun save(resources: Collection<Resource>) : Collection<Resource>
}
