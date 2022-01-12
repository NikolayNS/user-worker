package com.dmitrenko.userworker.client

import com.dmitrenko.userworker.exception.ClientResponseException
import com.dmitrenko.userworker.exception.ServerResponseException
import com.dmitrenko.userworker.model.request.WsoUserRequest
import com.dmitrenko.userworker.model.response.WsoUserResponse
import reactor.core.publisher.Mono

interface AsyncWsoClient {

	@Throws(ClientResponseException::class, ServerResponseException::class)
	fun getUserDetails(request: WsoUserRequest): Mono<WsoUserResponse>
}