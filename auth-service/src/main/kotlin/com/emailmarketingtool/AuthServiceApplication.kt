package com.emailmarketingtool

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.emailmarketingtool"])
@EnableDiscoveryClient
@EntityScan("com.emailmarketingtool.entities")
@EnableJpaRepositories(basePackages= ["com.emailmarketingtool.repositories"])
class AuthServiceApplication

fun main(args: Array<String>) {
	runApplication<AuthServiceApplication>(*args)
}
