import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}
repositories {
    mavenCentral()
}
dependencies {

}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
