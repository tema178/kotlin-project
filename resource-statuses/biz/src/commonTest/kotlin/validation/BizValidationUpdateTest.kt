package validation

import models.Command
import kotlin.test.Test

class BizValidationUpdateTest: BaseBizValidationTest() {
    override val command = Command.UPDATE

    @Test fun correctType() = validationTypeCorrect(command, processor)
    @Test fun trimType() = validationTypeTrim(command, processor)
    @Test fun emptyType() = validationTypeEmpty(command, processor)
    @Test fun badSymbolsType() = validationTypeSymbols(command, processor)

    @Test fun correctStatus() = validationStatusCorrect(command, processor)
    @Test fun trimStatus() = validationStatusTrim(command, processor)
    @Test fun emptyStatus() = validationStatusEmpty(command, processor)
    @Test fun badSymbolsStatus() = validationStatusSymbols(command, processor)

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

}
