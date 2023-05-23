
plugins {
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    id("java")
}

group = "plants"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

val springdocVersion = "2.0.2"

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("org.postgresql:postgresql")
    implementation("com.h2database:h2")
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springdoc:springdoc-openapi-ui:1.7.0")

    // Infrastructure
    implementation("org.flywaydb:flyway-core")


    // Test
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    enabled = false
}
