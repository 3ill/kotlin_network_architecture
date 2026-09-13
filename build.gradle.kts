import org.gradle.api.tasks.JavaExec

plugins {
    kotlin("jvm") version "2.4.20"
    kotlin("plugin.serialization") version "2.4.20"
    id("application")
}

tasks.named<JavaExec>("run") {
    mainClass.set("org.example.MainKt")
}

group = "org.example"
version = "1.0-SNAPSHOT"

val ktorVersion = "3.5.2"
val coroutinesVersion = "1.11.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-core:${ktorVersion}")
    implementation("io.ktor:ktor-client-okhttp:${ktorVersion}")
    implementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${ktorVersion}")
    implementation("io.ktor:ktor-client-websockets:${ktorVersion}")
    implementation("io.ktor:ktor-client-auth:${ktorVersion}")
    implementation("io.ktor:ktor-client-logging:${ktorVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}