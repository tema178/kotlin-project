package models

@JvmInline
value class UserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = UserId("")
    }
}
