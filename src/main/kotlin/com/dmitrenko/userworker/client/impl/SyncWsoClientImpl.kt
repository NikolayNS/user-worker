package com.dmitrenko.userworker.client.impl

import com.dmitrenko.userworker.client.BaseResponse
import com.dmitrenko.userworker.client.SyncWsoClient
import com.dmitrenko.userworker.model.request.WsoUserRequest
import com.dmitrenko.userworker.model.response.WsoUserResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import javax.validation.Valid

@Service
@Validated
class SyncWsoClientImpl(
	@Value("\${client.wso.user}")
	private var user: String,

	@Value("\${client.wso.password")
	private val password: String,

	@Value("\${client.wso.url.base-url")
	private val baseUrl: String,

	@Value("\${client.wso.url.user-details")
	private val userDetails: String,

	private val restTemplate: RestTemplate
) : SyncWsoClient {

	override fun getUserDetails(@Valid request: WsoUserRequest): BaseResponse<WsoUserResponse> {
		log.info("request body: {} ", request)

		return exchange(
			RequestEntity
				.post(
					UriComponentsBuilder
						.fromHttpUrl(baseUrl)
						.path(userDetails)
						.build()
						.toUri())
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.body(request), WsoUserResponse::class.java
		)
	}

	private fun <R> exchange(request: RequestEntity<*>, response: Class<R>): BaseResponse<R> {
		return try {
			request.headers.add("Authorization", String.format("Basic %s:%s", user, password))
			val exchange = restTemplate.exchange(request, response)
			log.info("Success request to url [{}] ", request.url)
			BaseResponse(exchange.body, null)
		} catch (e: RestClientException) {
			log.error("Error request to url [{}] ", request.url, e)
			BaseResponse(null, e)
		}
	}

	companion object {
		private val log: Logger = LoggerFactory.getLogger(this::class.java)
	}
}