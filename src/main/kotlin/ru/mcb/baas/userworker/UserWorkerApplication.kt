package ru.mcb.baas.userworker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserWorkerApplication

fun main(args: Array<String>) {
	runApplication<UserWorkerApplication>(*args)
}
