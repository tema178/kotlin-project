package repo

import models.Resource
import models.ResourceError

sealed interface IDbResourceResponse: IDbResponse<Resource>

data class DbResponseOk(
    val data: Resource
): IDbResourceResponse

data class DbResponseErr(
    val errors: List<ResourceError> = emptyList()
): IDbResourceResponse {
    constructor(err: ResourceError): this(listOf(err))
}

data class DbAdResponseErrWithData(
    val data: Resource,
    val errors: List<ResourceError> = emptyList()
): IDbResourceResponse {
    constructor(ad: Resource, err: ResourceError): this(ad, listOf(err))
}
