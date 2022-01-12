package com.dmitrenko.userworker.kafka.consumer

import com.dmitrenko.userworker.model.request.ApiUserRequest
import com.dmitrenko.userworker.service.UserService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import javax.validation.Valid

@Service
@Validated
class UserConsumer(
		private val userService: UserService
) {

	@KafkaListener(
			topics = ["#{'\${spring.kafka.topics.api-user-rq.name}'}"],
			groupId = "#{'\${spring.kafka.topics.api-user-rq.group-id}'}")
	fun listener(message: ConsumerRecord<String, @Valid ApiUserRequest>) {
		log.info("Consumer listen API: key=[{}]; value=[{}]", message.key(), message.value())

		userService.collectUserDetails(message.key(), message.value())
	}

	companion object {
		private val log: Logger = LoggerFactory.getLogger(this::class.java)
	}
}