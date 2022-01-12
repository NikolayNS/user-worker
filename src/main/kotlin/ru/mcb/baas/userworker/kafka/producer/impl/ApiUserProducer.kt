package ru.mcb.baas.userworker.kafka.producer.impl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import ru.mcb.baas.userworker.kafka.producer.Producer
import ru.mcb.baas.userworker.model.response.ApiUserResponse
import javax.validation.Valid

@Service
@Validated
class ApiUserProducer(
		private val kafkaTemplate: KafkaTemplate<String, ApiUserResponse>,

		@Value("\${spring.kafka.topics.api-user-rs.name}")
		private var topic: String
) : Producer<String, ApiUserResponse>{

	override fun sendMessage(key: String, @Valid value: ApiUserResponse) {
		kafkaTemplate.send(topic, key, value)

		log.info("Producer send: key=[{}]; value=[{}]", key, value)
	}

	companion object {
		private val log: Logger = LoggerFactory.getLogger(this::class.java)
	}
}