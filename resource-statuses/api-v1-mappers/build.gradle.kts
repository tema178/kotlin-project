plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(projects.apiV1)
    implementation(projects.resourceStatusesCommon)
    implementation(libs.kotlinx.datetime)
    testImplementation(kotlin("test-junit"))
}
