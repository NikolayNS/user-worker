package ru.mcb.baas.userworker.client

import reactor.core.publisher.Mono
import ru.mcb.baas.userworker.exception.ClientResponseException
import ru.mcb.baas.userworker.exception.ServerResponseException
import ru.mcb.baas.userworker.model.request.WsoUserRequest
import ru.mcb.baas.userworker.model.response.WsoUserResponse

interface AsyncWsoClient {

	@Throws(ClientResponseException::class, ServerResponseException::class)
	fun getUserDetails(request: WsoUserRequest): Mono<WsoUserResponse>
}