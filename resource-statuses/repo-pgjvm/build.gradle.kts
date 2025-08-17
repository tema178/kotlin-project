plugins {
    id("build-jvm")
}
repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(projects.resourceStatusesCommon)
    api(projects.repoCommon)

    implementation(libs.coroutines.core)
    implementation(libs.uuid)

    implementation(libs.db.postgres)
    implementation(libs.bundles.exposed)

    testImplementation(kotlin("test-junit"))
    testImplementation(projects.repoTests)
    testImplementation(libs.testcontainers.core)
    testImplementation(libs.logback)

}
