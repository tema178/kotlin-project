package models

@JvmInline
value class ResourceStatus(private val status: String) {

    fun asString() = status

    companion object {
        val NONE = ResourceStatus("")
    }
}