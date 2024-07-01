dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	implementation(project(mapOf("path" to ":")))
	implementation(project(":common"))
	testImplementation("org.flywaydb:flyway-core")
	testImplementation("org.testcontainers:junit-jupiter")
}
tasks.register<Wrapper>("wrapper") {
	gradleVersion = "8.5" // Ensure this matches the version you want to use
}