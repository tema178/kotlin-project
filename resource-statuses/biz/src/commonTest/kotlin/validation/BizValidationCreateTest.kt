package validation

import models.Command
import kotlin.test.Test

// смотрим пример теста валидации, собранного из тестовых функций-оберток
class BizValidationCreateTest: BaseBizValidationTest() {
    override val command: Command = Command.CREATE

    @Test fun correctType() = validationTypeCorrect(command, processor)
    @Test fun trimType() = validationTypeTrim(command, processor)
    @Test fun emptyType() = validationTypeEmpty(command, processor)
    @Test fun badSymbolsType() = validationTypeSymbols(command, processor)

    @Test fun correctStatus() = validationStatusCorrect(command, processor)
    @Test fun trimStatus() = validationStatusTrim(command, processor)
    @Test fun emptyStatus() = validationStatusEmpty(command, processor)
    @Test fun badSymbolsStatus() = validationStatusSymbols(command, processor)
}
