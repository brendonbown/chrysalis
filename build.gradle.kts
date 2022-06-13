import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    application
}

group = "me.bhb49"

// Don't forget to update this value in the CHANGELOG and in the `version` command
version = "0.1.0"

repositories {
    mavenCentral()
}

val exposedVersion: String by project
dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    implementation("com.oracle.database.jdbc:ojdbc8-production:21.5.0.0")

    implementation("com.xenomachina:kotlin-argparser:2.0.7")

    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // This is for logging the results of Exposed
    // (https://www.slf4j.org/codes.html#StaticLoggerBinder)
    //
    // The artifact ID can be any of the following:
    //   * slf4j-nop (current)
    //   * slf4j-simple
    implementation("org.slf4j:slf4j-nop:1.7.36")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

application {
    mainClass.set("MainKt")
}