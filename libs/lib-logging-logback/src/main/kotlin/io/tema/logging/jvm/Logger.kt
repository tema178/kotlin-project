import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * Generate internal LogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun loggerLogback(logger: Logger): ILogWrapper = LogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun loggerLogback(clazz: KClass<*>): ILogWrapper = loggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun loggerLogback(loggerId: String): ILogWrapper = loggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
