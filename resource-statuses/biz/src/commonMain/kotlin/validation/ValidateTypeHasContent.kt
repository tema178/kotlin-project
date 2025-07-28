package validation

import Context
import ICorChainDsl
import util.errorValidation
import util.fail
import worker

fun ICorChainDsl<Context>.validateTypeHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть какие-то слова в типе.
        Отказываем в публикации типов, в которых только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { !validating.type.isEmpty() && ! validating.type.contains(regExp) }
    handle {
        fail(
            errorValidation(
            field = "type",
            violationCode = "noContent",
            description = "field must contain letters"
        )
        )
    }
}
