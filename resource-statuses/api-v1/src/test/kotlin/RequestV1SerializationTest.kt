import org.tema.kotlin.resource.statuses.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = ResourceCreateRequest(
        debug = Debug(
            mode = RequestDebugMode.STUB,
            stub = RequestDebugStubs.BAD_TYPE
        ),
        resource =  ResourceCreateObject(
            id = "book№100",
            resourceType = "books",
            status = "available",
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"id\":\\s*\"book№100\""))
        assertContains(json, Regex("\"resourceType\":\\s*\"books\""))
        assertContains(json, Regex("\"status\":\\s*\"available\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badType\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as  ResourceCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"resource": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString,  ResourceCreateRequest::class.java)

        assertEquals(null, obj.resource)
    }
}
