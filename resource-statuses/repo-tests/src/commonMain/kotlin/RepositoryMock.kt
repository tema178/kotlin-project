import models.Resource
import repo.*

class RepositoryMock(
    private val invokeCreateAd: (DbRequest) -> IDbResourceResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeReadAd: (DbIdRequest) -> IDbResourceResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateAd: (DbRequest) -> IDbResourceResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteAd: (DbIdRequest) -> IDbResourceResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeSearchAd: (DbFilterRequest) -> IDbResponses = { DEFAULT_ADS_SUCCESS_EMPTY_MOCK },
): IRepo {
    override suspend fun create(rq: DbRequest): IDbResourceResponse {
        return invokeCreateAd(rq)
    }

    override suspend fun read(rq: DbIdRequest): IDbResourceResponse {
        return invokeReadAd(rq)
    }

    override suspend fun update(rq: DbRequest): IDbResourceResponse {
        return invokeUpdateAd(rq)
    }

    override suspend fun delete(rq: DbIdRequest): IDbResourceResponse {
        return invokeDeleteAd(rq)
    }

    override suspend fun search(rq: DbFilterRequest): IDbResponses {
        return invokeSearchAd(rq)
    }

    companion object {
        val DEFAULT_AD_SUCCESS_EMPTY_MOCK = DbResponseOk(Resource())
        val DEFAULT_ADS_SUCCESS_EMPTY_MOCK = DbResponsesOk(emptyList())
    }
}
