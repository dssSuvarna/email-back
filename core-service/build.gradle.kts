dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
	implementation("org.jobrunr:jobrunr-spring-boot-starter:5.3.3")
	implementation("org.freemarker:freemarker:2.3.32")
	implementation(project(":common"))
	implementation(project(mapOf("path" to ":")))
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-mysql")
	implementation ("org.jsoup:jsoup:1.17.2")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation ("org.json:json:20210307")
}
tasks.register<Wrapper>("wrapper") {
	gradleVersion = "8.5" // Ensure this matches the version you want to use
}