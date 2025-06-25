package org.tema.app.spring.stub

import Context
import org.tema.app.spring.config.Config
import org.tema.app.spring.controllers.StatusController
import StatusProcessor
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import org.tema.kotlin.resource.statuses.api.v1.models.*
import toTransportCreate
import toTransportDelete
import toTransportRead
import toTransportSearch
import toTransportUpdate
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(StatusController::class, Config::class)
internal class ControllerTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: StatusProcessor

    @Test
    fun createAd() = testStub(
        "/v1/statuses/create",
        ResourceCreateRequest(),
        Context().toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readAd() = testStub(
        "/v1/statuses/read",
        ResourceReadRequest(),
        Context().toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateAd() = testStub(
        "/v1/statuses/update",
        ResourceUpdateRequest(),
        Context().toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteAd() = testStub(
        "/v1/statuses/delete",
        ResourceDeleteRequest(),
        Context().toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchAd() = testStub(
        "/v1/statuses/search",
        ResourceSearchRequest(),
        Context().toTransportSearch().copy(responseType = "search")
    )

    private inline fun <reified Req : Any, reified Res : Any> testStub(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}
