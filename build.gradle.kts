plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "org.tema.kotlin.resource.statuses"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    repositories {
        mavenCentral()
    }
    group = rootProject.group
    version = rootProject.version
}

tasks {
    arrayOf("build", "clean", "check").forEach { tsk ->
        register(tsk) {
            group = "build"
            dependsOn(subprojects.map { it.getTasksByName(tsk, false) })
        }
    }
}

