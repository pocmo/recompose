import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.0"
    application
}
repositories {
    mavenCentral()
}
dependencies {
    implementation(project(":recompose-ast"))

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":recompose-parser"))
    testImplementation(project(":recompose-test"))
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

