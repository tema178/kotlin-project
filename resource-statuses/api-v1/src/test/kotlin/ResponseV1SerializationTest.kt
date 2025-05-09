import org.tema.kotlin.resource.statuses.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = ResourceCreateResponse(
        resource = ResourceResponseObject(
            id = "book№100",
            resourceType = "books",
            status = "available",
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"id\":\\s*\"book№100\""))
        assertContains(json, Regex("\"resourceType\":\\s*\"books\""))
        assertContains(json, Regex("\"status\":\\s*\"available\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as ResourceCreateResponse

        assertEquals(response, obj)
    }
}
