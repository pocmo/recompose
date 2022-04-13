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
    // https://mvnrepository.com/artifact/io.github.classgraph/classgraph
    implementation("io.github.classgraph:classgraph:4.8.143")

    implementation("xpp3:xpp3:1.1.4c")
    implementation("junit:junit:4.12")

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":recompose-test"))
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
