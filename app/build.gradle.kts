plugins {
    kotlin("jvm") version "1.9.10"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // More functional Kotlin (:
    implementation("io.arrow-kt:arrow-core:1.2.1")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("io.strikt:strikt-core:0.34.1")
    testImplementation("io.strikt:strikt-arrow:0.34.1")

    implementation(kotlin("script-runtime"))
}

tasks.test {
    useJUnitPlatform()
}
