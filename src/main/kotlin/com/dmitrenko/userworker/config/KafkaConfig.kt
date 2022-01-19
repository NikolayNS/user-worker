package com.dmitrenko.userworker.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.SeekToCurrentErrorHandler
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.util.backoff.FixedBackOff

@EnableKafka
@Configuration
class KafkaConfig(
		@Value("\${spring.kafka.properties.bootstrap-servers}")
		private var bootstrapServers: String,

		@Value("\${spring.kafka.retry.interval}")
		private val interval: Long,

		@Value("\${spring.kafka.retry.max-attempts}")
		private val maxAttempts: Long
) {

	@Bean
	fun <M> kafkaTemplate(): KafkaTemplate<String, M> {
		return KafkaTemplate(producerFactory<M>())
	}

	@Bean
	fun <M> producerFactory(): ProducerFactory<String, M> {
		val props = HashMap<String, Any>()
		props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
		props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
		props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
		return DefaultKafkaProducerFactory(props)
	}

	@Bean
	@Primary
	fun <M> kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, M> {
		val factory = ConcurrentKafkaListenerContainerFactory<String, M>()
		factory.consumerFactory = consumerFactory()
		factory.setConcurrency(1)
		factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL_IMMEDIATE
		factory.setErrorHandler(SeekToCurrentErrorHandler(FixedBackOff(interval, maxAttempts)))
		return factory
	}

	@Bean
	fun <M> consumerFactory(): ConsumerFactory<String?, M> {
		val props = HashMap<String, Any>()
		props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
		props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
		props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
		return DefaultKafkaConsumerFactory(props)
	}
}