package models

import kotlin.jvm.JvmInline

@JvmInline
value class ResourceType(private val type: String) {

    fun asString() = type

    companion object {
        val DEFAULT = ResourceType("common")
    }
}