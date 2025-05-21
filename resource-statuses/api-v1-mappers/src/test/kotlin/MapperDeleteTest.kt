import models.*
import org.junit.Test
import org.tema.kotlin.resource.statuses.api.v1.models.*
import stubs.Stubs
import kotlin.test.assertEquals

class MapperDeleteTest {

    @Test
    fun fromTransport() {
        val req = ResourceDeleteRequest(
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS,
            ),
            resource = ResourceStub.get().toTransportDeleteResource()
        )

        val context = Context()
        context.fromTransport(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals("PeaceAndWar", context.request.id.asString())
    }

    @Test
    fun toTransport() {
        val context = Context(
            requestId = RequestId("1234"),
            command = Command.DELETE,
            resource = ResourceStub.get(),
            errors = mutableListOf(
                ResourceError(
                    code = "err",
                    group = "request",
                    field = "type",
                    message = "wrong type",
                )
            ),
            state = State.RUNNING,
        )

        val req = context.toTransportResource() as ResourceDeleteResponse

        assertEquals(req.resource, ResourceStub.get().toTransportResource())
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("type", req.errors?.firstOrNull()?.field)
        assertEquals("wrong type", req.errors?.firstOrNull()?.message)
    }
}