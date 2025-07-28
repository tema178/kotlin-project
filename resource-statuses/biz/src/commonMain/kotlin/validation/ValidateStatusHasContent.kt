package validation

import Context
import ICorChainDsl
import util.errorValidation
import util.fail
import worker

fun ICorChainDsl<Context>.validateStatusHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть какие-то слова в статусе.
        Отказываем в публикации статусов, в которых только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { !validating.status.isEmpty() && ! validating.status.contains(regExp) }
    handle {
        fail(
            errorValidation(
            field = "status",
            violationCode = "noContent",
            description = "field must contain letters"
        )
        )
    }
}
