import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    `maven-publish`
}

repositories {
    maven(url = "https://jitpack.io")
    mavenCentral()
}

dependencies {
    implementation("com.github.bluedragonmc:messagingsystem:c9071e2f2c")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.bluedragonmc"
            artifactId = "messages"
            version = "0.0.4"

            from(components["java"])
        }
    }
}