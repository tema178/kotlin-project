import models.*
import org.junit.Test
import org.tema.kotlin.resource.statuses.api.v1.models.*
import stubs.Stubs

import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = ResourceCreateRequest(
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS,
            ),
            resource = ResourceCreateObject(
                id = "book100",
                resourceType = "book",
                status = "available"
            ),
        )

        val context = Context()
        context.fromTransport(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals("book100", context.request.id.asString())
    }

    @Test
    fun toTransport() {
        val context = Context(
            requestId = RequestId("1234"),
            command = Command.CREATE,
            resource = Resource(
                id = ResourceId("book100"),
                type = ResourceType("book"),
                status = ResourceStatus("available")
            ),
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

        val req = context.toTransportResource() as ResourceCreateResponse

        assertEquals("book100", req.resource?.id)
        assertEquals("book", req.resource?.resourceType)
        assertEquals("available", req.resource?.status)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("type", req.errors?.firstOrNull()?.field)
        assertEquals("wrong type", req.errors?.firstOrNull()?.message)
    }
}
