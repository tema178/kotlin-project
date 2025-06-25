plugins {
    application
    id("build-jvm")
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("ru.otus.otuskotlin.marketplace.app.kafka.MainKt")
}

docker {
    javaApplication {
        baseImage.set("openjdk:17.0.2-slim")
    }
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)

    implementation("libs:lib-logging-logback")

    // transport models
    implementation(project(":resource-statuses-common"))
    implementation(project(":app-common"))
    implementation(project(":api-v1"))
    implementation(project(":api-v1-mappers"))
    // logic
    implementation(project(":biz"))

    testImplementation(kotlin("test-junit"))
}
