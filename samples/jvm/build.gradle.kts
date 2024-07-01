plugins {
    kotlin("jvm") version "1.9.0"
}

group = "com.magnavisio.taficloud"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("com.magnavisio:taficloud-jvm:0.0.1")
//    implementation("com.github.MagnaVisioTechonlogies:TafiCloudLib:1.0.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}