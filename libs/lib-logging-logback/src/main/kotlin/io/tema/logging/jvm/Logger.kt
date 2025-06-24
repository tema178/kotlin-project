import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.marketplace.logging.common.IMpLogWrapper
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

fun loggerLogback(clazz: KClass<*>): IMpLogWrapper = loggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun loggerLogback(loggerId: String): IMpLogWrapper = loggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
