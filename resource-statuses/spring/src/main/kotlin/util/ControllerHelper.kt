package io.tema.app.spring.util

import Context
import io.tema.app.spring.config.IStatusAppSettings
import kotlinx.datetime.Clock
import models.State
import kotlin.reflect.KClass

suspend inline fun <T> IStatusAppSettings.controllerHelper(
    crossinline getRequest: suspend Context.() -> Unit,
    crossinline toResponse: suspend Context.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
//    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = Context(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
//        logger.info(
//            msg = "Request $logId started for ${clazz.simpleName}",
//            marker = "BIZ",
//            data = ctx.toLog(logId)
//        )
        processor.exec(ctx)
//        logger.info(
//            msg = "Request $logId processed for ${clazz.simpleName}",
//            marker = "BIZ",
//            data = ctx.toLog(logId)
//        )
        ctx.toResponse()
    } catch (e: Throwable) {
//        logger.error(
//            msg = "Request $logId failed for ${clazz.simpleName}",
//            marker = "BIZ",
//            data = ctx.toLog(logId)
//        )
        ctx.state = State.FAILING
        ctx.errors.add(e.asResourceError())
        processor.exec(ctx)
        ctx.toResponse()
    }
}
