package com.dmitrenko.userworker.client.impl

import com.dmitrenko.userworker.client.AsyncWsoClient
import com.dmitrenko.userworker.exception.ClientResponseException
import com.dmitrenko.userworker.exception.ClientRetryException
import com.dmitrenko.userworker.exception.ServerResponseException
import com.dmitrenko.userworker.model.request.WsoUserRequest
import com.dmitrenko.userworker.model.response.WsoUserResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import reactor.util.retry.Retry.RetrySignal
import reactor.util.retry.RetryBackoffSpec
import java.time.Duration
import javax.validation.Valid

@Service
@Validated
class AsyncWsoClientImpl(
	@Value("\${client.wso.url.user-details")
	private val userDetails: String,

	private val webClient: WebClient
) : AsyncWsoClient {

	override fun getUserDetails(@Valid request: WsoUserRequest): Mono<WsoUserResponse> {
		log.info("request body: {} ", request)

		return webClient
			.post()
			.uri(UriComponentsBuilder.fromHttpUrl(userDetails).build().toUri())
			.contentType(APPLICATION_JSON)
			.accept(APPLICATION_JSON)
			.body(Mono.just(request), request::class.java)
			.retrieve()
			.onStatus({ h: HttpStatus -> h.is4xxClientError }, { r: ClientResponse? -> clientExceptionResponse(r!!) })
			.onStatus({ h: HttpStatus -> h.is5xxServerError }, { r: ClientResponse? -> serverExceptionResponse(r!!) })
			.bodyToMono(WsoUserResponse::class.java)
			.retryWhen(Retry
				.backoff(3, Duration.ofSeconds(5))
				.filter { obj: Throwable -> ServerResponseException::class.java.isInstance(obj) }
				.onRetryExhaustedThrow { b: RetryBackoffSpec, s: RetrySignal ->
					throw ClientRetryException("External Service failed to process after max retries", HttpStatus.SERVICE_UNAVAILABLE.value())
				})
	}

	private fun clientExceptionResponse(response: ClientResponse): Mono<out Throwable> {
		return response.bodyToMono(String::class.java)
			.map { body: String -> ClientResponseException(response.statusCode(), response.headers().asHttpHeaders(), body) }
	}

	private fun serverExceptionResponse(response: ClientResponse): Mono<out Throwable> {
		return response.bodyToMono(String::class.java)
			.map{ body: String -> ServerResponseException(response.statusCode(), response.headers().asHttpHeaders(), body) }
	}

	companion object {
		private val log: Logger = LoggerFactory.getLogger(this::class.java)
	}
}