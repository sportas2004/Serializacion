rootProject.name = "API"
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    // ESTE PLUGIN SOLUCIONA EL ERROR "Toolchain repositories not configured"
    // Permite que Gradle descargue el JDK 21 automáticamente
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "MiProyectoKotlin"