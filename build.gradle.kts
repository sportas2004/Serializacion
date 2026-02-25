plugins {
    // Versiones alineadas de Kotlin y Serialización
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Librería JSON necesaria para tu código
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Librería de testing básica
    testImplementation(kotlin("test"))
}

// --- CONFIGURACIÓN CRÍTICA ---
kotlin {
    // Esto obliga a usar Java 21.
    // Gracias al archivo settings.gradle.kts, si no lo tienes, se descargará solo.
    jvmToolchain(21)
}

application {
    // Asegúrate de que tu Main.kt tenga 'package org.example' al principio
    mainClass.set("org.example.MainKt")
}