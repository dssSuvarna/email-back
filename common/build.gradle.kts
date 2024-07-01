dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("org.mockito:mockito-inline:5.2.0")
}

tasks.named("bootJar").configure {
    enabled = false
}
tasks.register<Wrapper>("wrapper") {
    gradleVersion = "8.5" // Ensure this matches the version you want to use
}