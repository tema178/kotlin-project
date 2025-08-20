import models.Resource
import repo.*

class RepositoryMock(
    private val invokeCreateRes: (DbRequest) -> IDbResourceResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeReadRes: (DbIdRequest) -> IDbResourceResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateRes: (DbRequest) -> IDbResourceResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteRes: (DbIdRequest) -> IDbResourceResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeSearchRes: (DbFilterRequest) -> IDbResponses = { DEFAULT_ADS_SUCCESS_EMPTY_MOCK },
): IRepo {
    override suspend fun create(rq: DbRequest): IDbResourceResponse {
        return invokeCreateRes(rq)
    }

    override suspend fun read(rq: DbIdRequest): IDbResourceResponse {
        return invokeReadRes(rq)
    }

    override suspend fun update(rq: DbRequest): IDbResourceResponse {
        return invokeUpdateRes(rq)
    }

    override suspend fun delete(rq: DbIdRequest): IDbResourceResponse {
        return invokeDeleteRes(rq)
    }

    override suspend fun search(rq: DbFilterRequest): IDbResponses {
        return invokeSearchRes(rq)
    }

    companion object {
        val DEFAULT_AD_SUCCESS_EMPTY_MOCK = DbResponseOk(Resource())
        val DEFAULT_ADS_SUCCESS_EMPTY_MOCK = DbResponsesOk(emptyList())
    }
}
