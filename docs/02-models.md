
## Описание сущностей приложения

| Сущность             | Описание                                                                               |
|:---------------------|:---------------------------------------------------------------------------------------|
| **Resource**         | Объект ресурса. Содержит тип объекта и статус.                                         |
| **StatusDictionary** | Словарь статусов. Содержит множество возможных статусов для конкретного типа объектов. |
| **Status**           | Отдельный статус в рамках словаря: имеет код, название и описание.                     |
| **Type**             | Тип объекта (например, "Заявка", "Пользователь", "Проект").                            |


**Resource**

- id
- type
- status
- updatedBy
- updatedAt

**StatusDictionary**

- id
- name
- description
- statuses

**Type**

- id
- name
- description
- statusDictionary

## API

**Resource** - crud + findById, findByType, findByStatus

**StatusDictionary** - crud

**Type** - crud