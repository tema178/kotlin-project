package repo

interface IRepo {
    suspend fun create(rq: DbRequest): IDbResourceResponse
    suspend fun read(rq: DbIdRequest): IDbResourceResponse
    suspend fun update(rq: DbRequest): IDbResourceResponse
    suspend fun delete(rq: DbIdRequest): IDbResourceResponse
    suspend fun search(rq: DbFilterRequest): IDbResponses
    companion object {
        val NONE = object : IRepo {
            override suspend fun create(rq: DbRequest): IDbResourceResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun read(rq: DbIdRequest): IDbResourceResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun update(rq: DbRequest): IDbResourceResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun delete(rq: DbIdRequest): IDbResourceResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun search(rq: DbFilterRequest): IDbResponses {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
