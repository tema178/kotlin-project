

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "pass",
    val database: String = "resource_statuses",
    val schema: String = "public",
    val table: String = "statuses",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
