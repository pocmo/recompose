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
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.13.1")
}
