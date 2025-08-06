import models.Resource
import repo.IRepo

interface IRepoInitializable: IRepo {
    fun save(ads: Collection<Resource>) : Collection<Resource>
}
