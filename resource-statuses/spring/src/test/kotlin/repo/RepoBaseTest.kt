package org.tema.app.spring.repo

import Context
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import models.*
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import org.tema.kotlin.resource.statuses.api.v1.models.*
import toTransportCreate
import toTransportCreateResource
import toTransportDelete
import toTransportDeleteResource
import toTransportRead
import toTransportReadResource
import toTransportSearch
import toTransportUpdate
import toTransportUpdateResource
import java.math.BigDecimal
import kotlin.test.Test

internal abstract class RepoBaseTest {
    protected abstract var webClient: WebTestClient
    private val debug = Debug(mode = RequestDebugMode.TEST)
    protected val uuidNew = "10000000-0000-0000-0000-000000000001"
    protected val updatedAtStub: Instant = Clock.System.now()

    @Test
    open fun create() = testRepo(
        "create",
        ResourceCreateRequest(
            resource = ResourceStub.get().toTransportCreateResource(),
            debug = debug,
        ),
        prepareCtx(ResourceStub.prepareResult {
            id = ResourceId(uuidNew)
            updatedBy = UserId.NONE
            updatedAt = updatedAtStub
        })
            .toTransportCreate()
            .copy(responseType = "create")
    )

    @Test
    open fun read() = testRepo(
        "read",
        ResourceReadRequest(
            resource = ResourceStub.get().toTransportReadResource(),
            debug = debug,
        ),
        prepareCtx(ResourceStub.get().copy(updatedAt = updatedAtStub))
            .toTransportRead()
            .copy(responseType = "read")
    )

    @Test
    open fun update() = testRepo(
        "update",
        ResourceUpdateRequest(
            resource = ResourceStub.prepareResult { type = ResourceType("add") }.toTransportUpdateResource(),
            debug = debug,
        ),
        prepareCtx(ResourceStub.prepareResult {
            type = ResourceType("add")
            updatedAt = updatedAtStub})
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    open fun delete() = testRepo(
        "delete",
        ResourceDeleteRequest(
            resource = ResourceStub.get().toTransportDeleteResource(),
            debug = debug,
        ),
        prepareCtx(ResourceStub.get().copy(updatedAt = updatedAtStub))
            .toTransportDelete()
            .copy(responseType = "delete")
    )

    @Test
    open fun search() = testRepo(
        "search",
        ResourceSearchRequest(
            resourceFilter = ResourceSearchFilter(resourceType = "xx"),
            debug = debug,
        ),
        Context(
            state = State.RUNNING,
            resources = ResourceStub.prepareSearchList(ResourceType("xx"), updatedAtStub)
                .sortedBy { it.id.asString() }
                .toMutableList()
        )
            .toTransportSearch().copy(responseType = "search")
    )


    private fun prepareCtx(resource: Resource) = Context(
        state = State.RUNNING,
        resource = resource.apply {
            // Пока не реализована эта функциональность
        },
    )

    private inline fun <reified Req : Any, reified Res : IResponse> testRepo(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v1/statuses/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                val sortedResp: IResponse = when (it) {
                    is ResourceSearchResponse -> it.copy(resources = it.resources?.sortedBy { it.id }?.map { it.copy(updatedAt = BigDecimal.valueOf(updatedAtStub.epochSeconds)) })
                    is ResourceCreateResponse -> it.copy(resource = it.resource?.copy(updatedAt = BigDecimal.valueOf(updatedAtStub.epochSeconds)))
                    is ResourceReadResponse -> it.copy(resource = it.resource?.copy(updatedAt = BigDecimal.valueOf(updatedAtStub.epochSeconds)))
                    is ResourceDeleteResponse -> it.copy(resource = it.resource?.copy(updatedAt = BigDecimal.valueOf(updatedAtStub.epochSeconds)))
                    is ResourceUpdateResponse -> it.copy(resource = it.resource?.copy(updatedAt = BigDecimal.valueOf(updatedAtStub.epochSeconds)))
                    else -> it
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
    }
}
