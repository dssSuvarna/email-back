package com.emailmarketingtool.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class EmailMarketingToolGatewayApplication

fun main(args: Array<String>) {
	runApplication<EmailMarketingToolGatewayApplication>(*args)
}
