package models

import LogLevel

data class ResourceError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
    val level: LogLevel = LogLevel.ERROR,
)
