package exceptions

import models.WorkMode

class DbNotConfiguredException(val workMode: WorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)
