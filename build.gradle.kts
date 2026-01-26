group = "luna.nodes"
version = System.getenv("GITHUB_SHA")?.take(7) ?: ""

plugins {
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    id("org.jlleitschuh.gradle.ktlint") version "14.0.1"
    id("dev.detekt") version "2.0.0-alpha.1"
}

java.toolchain.languageVersion = JavaLanguageVersion.of(25)

// detekt doesn't yet support java 25, so we must force jvm to 24 for its task
tasks.withType<dev.detekt.gradle.Detekt>().configureEach {
    jvmTarget = "24"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom:2026.01.08-1.21.11")

    // testing
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.2")
    testImplementation("org.slf4j:slf4j-simple:2.0.17") // logging (only used while testing at the moment)
}

tasks.test {
    useJUnitPlatform()

    systemProperty("keepRunning", System.getProperty("keepRunning", "false"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/d-z4/minestom-nodes")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
