import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    application
}
repositories {
    mavenCentral()
}
dependencies {
    implementation(project(":recompose-ast"))
    implementation(project(":recompose-parser"))
    implementation(project(":recompose-composer"))
    implementation("com.github.ajalt.clikt:clikt:3.0.1")
    testImplementation(kotlin("test-junit"))
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
application {
    mainClassName = "MainKt"
}
