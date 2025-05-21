package models

import kotlin.jvm.JvmInline

@JvmInline
value class Lock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = Lock("")
    }
}
