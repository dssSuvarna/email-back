dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    testImplementation("io.projectreactor:reactor-test")
}
tasks.register<Wrapper>("wrapper") {
    gradleVersion = "8.5" // Ensure this matches the version you want to use
}