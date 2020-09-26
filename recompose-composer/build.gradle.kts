import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
}
repositories {
    mavenCentral()
}
dependencies {
    implementation(project(":recompose-ast", "default"))

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":recompose-parser", "default"))
    testImplementation(project(":recompose-test"))
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
