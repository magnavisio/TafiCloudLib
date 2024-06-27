plugins {
    kotlin("jvm") version "1.9.0"
}

group = "com.magnavisio.taficloud"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("com.magnavisio:taficloud-jvm:0.0.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}