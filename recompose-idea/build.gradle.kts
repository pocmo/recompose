import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea apply true
    id("java")
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.intellij") version "1.5.3"
    id("org.jetbrains.changelog") version "1.3.1"
}

project.group = "xyz.pocmo.recompose"
project.version = "1.0"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(project(":recompose-ast", "default"))
    implementation(project(":recompose-parser", "default"))
    implementation(project(":recompose-composer", "default"))
}

intellij {
    version.set("2022.1")
    updateSinceUntilBuild.set(false)
    updateSinceUntilBuild.set(false)
    downloadSources.set(true)
    plugins.set(listOf("IntelliLang", "Kotlin"))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
            freeCompilerArgs = listOf("-progressive")
        }
    }

    patchPluginXml {
        sinceBuild.set("193")
        untilBuild.set("202.*")
    }
    runPluginVerifier {
        ideVersions.set(listOf("2022.1"))
    }
}

