plugins {
    kotlin("jvm") version "1.4.32"
}

group = "com.github.csdbianhua"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.4.32")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.1")
}
