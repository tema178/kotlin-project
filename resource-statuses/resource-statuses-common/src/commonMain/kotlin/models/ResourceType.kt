package models

import kotlin.jvm.JvmInline

@JvmInline
value class ResourceType(private val type: String) {

    fun asString() = type

    companion object {
        val DEFAULT = ResourceType("common")
        val NONE = ResourceType("")
    }

    fun isEmpty() = type.isEmpty()

    fun contains(name: Regex): Boolean = type.contains(name)
}