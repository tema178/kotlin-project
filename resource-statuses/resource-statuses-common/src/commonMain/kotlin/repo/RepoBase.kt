package repo

import util.errorSystem

abstract class RepoBase: IRepo {

    protected suspend fun tryMethod(block: suspend () -> IDbResourceResponse) = try {
        block()
    } catch (e: Throwable) {
        DbResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryResourcesMethod(block: suspend () -> IDbResponses) = try {
        block()
    } catch (e: Throwable) {
        DbResourcesResponseErr(errorSystem("methodException", e = e))
    }

}
