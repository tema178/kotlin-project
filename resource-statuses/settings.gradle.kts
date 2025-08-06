rootProject.name = "resource-statuses"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../plugins")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

// Включает вот такую конструкцию
//implementation(projects.m2l5Gradle.sub1.ssub1)
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":resource-statuses-common")
include(":app-common")
include(":api-v1")
include(":api-v1-mappers")
include(":stubs")
include(":spring")
include(":log")
include(":kafka")
include(":biz")
include(":repo-common")
include(":repo-tests")
include(":repo-inmemory")
