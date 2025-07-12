package org.tema.app.spring.controllers

import IStatusAppSettings
import ResourceStub
import controllerHelper
import fromTransport
import org.tema.app.spring.config.StatusAppSettings
import models.Resource
import org.springframework.web.bind.annotation.*
import org.tema.kotlin.resource.statuses.api.v1.models.*
import toTransportResource
import kotlin.reflect.KClass

@RestController
@RequestMapping("/v1/statuses")
class StatusController(
    private val appSettings: StatusAppSettings
) {

    @GetMapping("get")
    suspend fun get(): Resource =
        ResourceStub.get()


    @PostMapping("create")
    suspend fun create(@RequestBody request: ResourceCreateRequest): ResourceCreateResponse =
        process(appSettings, request = request, this::class, "create")


    @PostMapping("read")
    suspend fun read(@RequestBody request: ResourceReadRequest): ResourceReadResponse =
        process(appSettings, request = request, this::class, "read")

    @PostMapping("update")
    suspend fun update(@RequestBody request: ResourceUpdateRequest): ResourceUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun delete(@RequestBody request: ResourceDeleteRequest): ResourceDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun search(@RequestBody request: ResourceSearchRequest): ResourceSearchResponse =
        process(appSettings, request = request, this::class, "search")


companion object {
    suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
        appSettings: IStatusAppSettings,
        request: Q,
        clazz: KClass<*>,
        logId: String,
    ): R = appSettings.controllerHelper(
        {
            fromTransport(request)
        },
        { toTransportResource() as R },
        clazz,
        logId,
    )
}
}
