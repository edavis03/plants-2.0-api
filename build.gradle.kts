
plugins {
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    id("java")
}

group = "plants"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

val springdocVersion = "2.0.4"
val lombokVersion = "1.18.26"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    runtimeOnly("org.postgresql:postgresql")
    implementation("com.h2database:h2")
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocVersion")

    // Infrastructure
    implementation("org.flywaydb:flyway-core")


    // Test
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testImplementation ("org.instancio:instancio-junit:2.16.0"  )
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    enabled = false
}
