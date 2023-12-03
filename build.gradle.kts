plugins {
    kotlin("jvm") version "1.9.0"
    // Plugin for Dokka - KDoc generating tool
    id("org.jetbrains.dokka") version "1.9.10"
    // Code coverage tool
    jacoco
    // Plugin for Ktlint
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
    application
}
group = "niallmruane.recipe"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
    // report is always generated after tests run
    finalizedBy(tasks.jacocoTestReport)
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

dependencies {
    testImplementation(kotlin("test"))
    // dependencies for logging
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    // For Streaming to XML JSON and YAML
    implementation("com.thoughtworks.xstream:xstream:1.4.18")
    implementation("org.codehaus.jettison:jettison:1.4.1")
    implementation("org.yaml:snakeyaml:1.33")
    // https://mvnrepository.com/artifact/org.yaml/snakeyaml

    // For generating a Dokka Site from KDoc
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.9.10")
}
