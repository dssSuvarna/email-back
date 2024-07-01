package com.emailmarketingtool.eurekaserverregistry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
@EnableEurekaServer
class EurekaServerRegistryApplication

fun main(args: Array<String>) {
	runApplication<EurekaServerRegistryApplication>(*args)
}
