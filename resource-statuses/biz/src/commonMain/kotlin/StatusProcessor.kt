import general.initStatus
import general.operation
import general.stubs
import models.*
import repo.*
import stubs.*
import validation.*

@Suppress("unused")
class StatusProcessor(private val corSettings: CorSettings = CorSettings.NONE) {

    suspend fun exec(ctx: Context) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<Context> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

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
            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание объявления в БД")
            }
            prepareResult("Подготовка ответа")
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
            chain {
                title = "Логика чтения"
                repoRead("Чтение объявления из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == State.RUNNING }
                    handle { adRepoDone = adRepoRead }
                }
            }
            prepareResult("Подготовка ответа")
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

            chain {
                title = "Логика сохранения"
                repoRead("Чтение объявления из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление объявления в БД")
            }
            prepareResult("Подготовка ответа")
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

            chain {
                title = "Логика удаления"
                repoRead("Чтение объявления из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление объявления из БД")
            }
            prepareResult("Подготовка ответа")
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

            repoSearch("Поиск объявления в БД по фильтру")
            prepareResult("Подготовка ответа")
        }

    }.build()
}
