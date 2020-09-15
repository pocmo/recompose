import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}
repositories {
    mavenCentral()
}
dependencies {
    implementation(project(":recompose-ast"))
    implementation("xpp3:xpp3:1.1.4c")
    implementation("junit:junit:4.12")

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":recompose-test"))
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
