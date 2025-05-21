package models

import kotlin.jvm.JvmInline

@JvmInline
value class ResourceId(private val id: String) {

    fun asString() = id

    companion object {
        val NONE = ResourceId("")
    }
}