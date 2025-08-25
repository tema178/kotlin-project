object SqlFields {
    const val ID = "id"
    const val NAME = "name"
    const val TYPE = "type"
    const val STATUS = "status"
    const val UPDATED_BY_ID = "updated_by_id"
    const val UPDATED_AT = "updated_at"
    const val LOCK = "lock"

    const val FILTER_TYPE = TYPE
    const val FILTER_STATUS = STATUS
    const val FILTER_OWNER_ID = UPDATED_BY_ID

    const val DELETE_OK = "DELETE_OK"

    fun String.quoted() = "\"$this\""
    val allFields = listOf(
        ID, TYPE, STATUS, UPDATED_BY_ID
    )
}
