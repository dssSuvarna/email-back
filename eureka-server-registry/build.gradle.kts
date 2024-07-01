dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
}
tasks.register<Wrapper>("wrapper") {
    gradleVersion = "8.5" // Ensure this matches the version you want to use
}