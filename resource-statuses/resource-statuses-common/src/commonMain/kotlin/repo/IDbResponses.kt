package repo

import models.Resource
import models.ResourceError

sealed interface IDbResponses: IDbResponse<List<Resource>>

data class DbResponsesOk(
    val data: List<Resource>
): IDbResponses

@Suppress("unused")
data class DbAdsResponseErr(
    val errors: List<ResourceError> = emptyList()
): IDbResponses {
    constructor(err: ResourceError): this(listOf(err))
}
