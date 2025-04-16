plugins {
    kotlin("jvm") apply false
}

group = "org.tema"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
    repositories{
        mavenCentral()
    }
}