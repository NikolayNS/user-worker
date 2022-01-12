package ru.mcb.baas.userworker.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
		@Value("\${client.http.connect-timeout}")
		private val connectTimeout: Int,

		@Value("\${client.http.read-timeout}")
		private val readTimeout: Int,

		@Value("\${client.wso.url.base-url")
		private val baseWsoUrl: String,

		@Value("\${client.wso.user}")
		private var user: String,

		@Value("\${client.wso.password")
		private val password: String,
) {

	@Bean
	fun restTemplate(): RestTemplate {
		val clientHttpRequestFactory = SimpleClientHttpRequestFactory()
		clientHttpRequestFactory.setConnectTimeout(connectTimeout)
		clientHttpRequestFactory.setReadTimeout(readTimeout)
		return RestTemplate(clientHttpRequestFactory)
	}

	@Bean
	@Primary
	fun webClient(): WebClient {
		return WebClient.builder()
			.baseUrl(baseWsoUrl)
			.defaultHeader("Authorization", String.format("Basic %s:%s", user, password))
			.build()
	}
}