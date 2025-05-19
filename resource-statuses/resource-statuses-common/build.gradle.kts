plugins {
    id("build-kmp")
}
kotlin {
    sourceSets {
        val coroutinesVersion: String by project
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(libs.kotlinx.datetime)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        jvmTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        nativeTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}