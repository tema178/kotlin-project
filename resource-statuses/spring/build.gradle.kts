plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencies)
    alias(libs.plugins.spring.kotlin)
    id("build-jvm")
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.spring.actuator)
    implementation(libs.spring.webflux)
    implementation(libs.spring.webflux.ui)
    implementation(libs.jackson.kotlin)
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.reactor)
    implementation(libs.coroutines.reactive)

    // Внутренние модели
    implementation(project(":resource-statuses-common"))
    implementation(project(":app-common"))
    implementation(project(":stubs"))
    implementation(project(":api-v1-mappers"))
    implementation(project(":api-v1"))

    implementation("libs:lib-logging-logback")
    implementation(projects.biz)
    implementation(projects.repoInmemory)
    implementation(projects.repoPgjvm)

    // tests
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.spring.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.spring.mockk)


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}


tasks {
    withType<ProcessResources> {
        val files = listOf("spec-v1").map {
            rootProject.ext[it]
        }
        from(files) {
            into("/static")
            filter {
                // Устанавливаем версию в сваггере
                it.replace("\${VERSION_APP}", project.version.toString())
            }

        }
    }
}

tasks.test {
    useJUnitPlatform()
    environment("MKPLADS_DB", "test_db")
}

tasks.bootBuildImage {
    builder = "paketobuildpacks/builder-jammy-base:latest"
    environment.set(mapOf("BP_HEALTH_CHECKER_ENABLED" to "true"))
    buildpacks.set(
        listOf(
            "gcr.io/paketo-buildpacks/adoptium",
            "urn:cnb:builder:paketo-buildpacks/java",
            "gcr.io/paketo-buildpacks/health-checker:latest"
        )
    )

}