import com.android.builder.model.AndroidLibrary
import org.jetbrains.kotlin.fir.resolve.withExpectedType

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("module.publication")
    `maven-publish`
}

group = "com.magnavisio"
version = "0.0.1"

kotlin {
    targetHierarchy.default()
    jvm()
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
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
        println(this)
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
