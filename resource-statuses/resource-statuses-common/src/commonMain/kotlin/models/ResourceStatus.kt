package models

import kotlin.jvm.JvmInline

@JvmInline
value class ResourceStatus(private val status: String) {

    fun asString() = status

    companion object {
        val NONE = ResourceStatus("")
    }

    fun isEmpty(): Boolean = status.isEmpty()

    fun contains(name: Regex): Boolean = status.contains(name)

}