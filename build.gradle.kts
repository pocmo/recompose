buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

plugins {
    kotlin("jvm") version "1.4.0"
    id("io.gitlab.arturbosch.detekt") version "1.13.1"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.0"
}
