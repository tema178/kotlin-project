package validation

import CorSettings
import StatusProcessor
import models.Command


abstract class BaseBizValidationTest {
    protected abstract val command: Command
    private val settings by lazy { CorSettings() }
    protected val processor by lazy { StatusProcessor(settings) }
}
