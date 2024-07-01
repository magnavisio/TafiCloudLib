plugins {
    `kotlin-dsl`
    `maven-publish`
}

dependencies {
    implementation(libs.nexus.publish)
}