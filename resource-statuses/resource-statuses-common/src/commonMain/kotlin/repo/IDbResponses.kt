package repo

import models.Resource
import models.ResourceError

sealed interface IDbResponses: IDbResponse<List<Resource>>

data class DbResponsesOk(
    val data: List<Resource>
): IDbResponses

@Suppress("unused")
data class DbResourcesResponseErr(
    val errors: List<ResourceError> = emptyList()
): IDbResponses {
    constructor(err: ResourceError): this(listOf(err))
}
