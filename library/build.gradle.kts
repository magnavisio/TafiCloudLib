import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
//    id("module.publication")
    id("maven-publish")
    kotlin("plugin.serialization") version libs.versions.kotlin
    id("org.jetbrains.kotlin.native.cocoapods")
}

group = "com.magnavisio"
version = "0.0.1"

kotlin {
    targetHierarchy.default()
    jvm()
    js(IR) {
        browser()
        nodejs()
    }
//    androidTarget {
//        publishLibraryVariants("release")
//        compilations.all {
//            kotlinOptions {
//                jvmTarget = "1.8"
//            }
//        }
//    }

    val xcFrameworkName = "TafiCloud"
    val xcf = XCFramework(xcFrameworkName)
    listOf(
        iosX64(),
        iosArm64(),
        // iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = xcFrameworkName

            // Specify CFBundleIdentifier to uniquely identify the framework
            binaryOption("bundleId", "com.magnavisio.${xcFrameworkName}")
            xcf.add(this)
            isStatic = true
        }
    }

    cocoapods {
        summary = "TafiCloud Library for iOS"
        homepage = "https://docs.taficloud.com"
        ios.deploymentTarget = "11.0"
        framework {
            baseName = "TafiCloudIOS"
        }
    }

    linuxX64()


    sourceSets {
        val ktorVersion = "2.3.12"

        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-apache5:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:1.5.5")

            }
        }

        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktorVersion")
            }
        }

        val nativeMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }

        sourceSets {
            val iosMain by getting
            val iosTest by getting
        }

//        val androidMain by getting {
//            dependencies {
//                implementation("io.ktor:ktor-client-android:$ktorVersion")
//            }
//        }

    }
}

android {
    namespace = "com.magnavisio.taficloud"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}


publishing {
    publications {
        withType<MavenPublication> {
            val target = this.name
            if (target == "kotlinMultiplatform") {
                artifactId = "taficloud"
            } else {
                artifactId = "taficloud-$target"
//                artifact(javaDocJar)
//                from(components["java"])
//                artifact(tasks.sourcesJar)
            }
        }

    }
}
