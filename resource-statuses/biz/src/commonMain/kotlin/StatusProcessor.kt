import general.initStatus
import general.operation
import general.stubs
import models.Command
import models.ResourceId
import models.ResourceStatus
import models.ResourceType
import stubs.*
import validation.*

@Suppress("unused")
class StatusProcessor(private val corSettings: CorSettings = CorSettings.NONE) {

    suspend fun exec(ctx: Context) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<Context> {
        initStatus("Инициализация статуса")

        operation("Добавление нового ресурса", Command.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadType("Имитация ошибки валидации типа")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в validating") { validating = request.copy() }
                worker("Очистка id") { validating.id = ResourceId.NONE }
                worker("Очистка типа") { validating.type = ResourceType(validating.type.asString().trim()) }
                worker("Очистка статус") { validating.status = ResourceStatus(validating.status.asString().trim()) }
                validateTypeNotEmpty("Проверка, что тип не пуст")
                validateTypeHasContent("Проверка символов")
                validateStatusNotEmpty("Проверка, что статус не пустой")
                validateStatusHasContent("Проверка символов")

                finishValidation("Завершение проверок")
            }
        }
        operation("Получить статуса ресурса", Command.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в validating") { validating = request.copy() }
                worker("Очистка id") { validating.id = ResourceId(validating.id.asString().trim())  }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Изменить статус", Command.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadType("Имитация ошибки валидации типа")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в validating") { validating = request.copy() }
                worker("Очистка id") { validating.id = ResourceId(validating.id.asString().trim()) }
                worker("Очистка типа") { validating.type = ResourceType(validating.type.asString().trim()) }
                worker("Очистка статус") { validating.status = ResourceStatus(validating.status.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateTypeNotEmpty("Проверка, что тип не пуст")
                validateTypeHasContent("Проверка символов")
                validateStatusNotEmpty("Проверка, что статус не пустой")
                validateStatusHasContent("Проверка символов")

                finishValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Удалить ресурс", Command.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в validating") { validating = request.copy() }
                worker("Очистка id") { validating.id = ResourceId(validating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Поиск ресурса по типу", Command.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в filterValidating") { filterValidating = filterRequest.copy() }

                finishFilterValidation("Успешное завершение процедуры валидации")
            }
        }

    }.build()
}
