plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Зависимость для работы с JSON
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    // Зависимости для тестирования (JUnit)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}