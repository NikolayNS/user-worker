package ru.mcb.baas.userworker.service.impl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.mcb.baas.userworker.client.AsyncWsoClient
import ru.mcb.baas.userworker.kafka.producer.Producer
import ru.mcb.baas.userworker.mapper.ApiUserResponseMapper
import ru.mcb.baas.userworker.mapper.WsoUserRequestMapper
import ru.mcb.baas.userworker.model.request.ApiUserRequest
import ru.mcb.baas.userworker.model.response.ApiUserResponse
import ru.mcb.baas.userworker.service.UserService

@Service
class UserServiceImpl(
	private val asyncWsoClient: AsyncWsoClient,
	private val producer: Producer<String, ApiUserResponse>
) : UserService {

	private lateinit var wsoUserRequestMapper: WsoUserRequestMapper
	private lateinit var apiUserResponseMapper: ApiUserResponseMapper

	override fun collectUserDetails(key: String, value: ApiUserRequest) {
		log.info("Start collecting user information, key=[{}]; value=[{}]", key, value)

		val wsoRequest = wsoUserRequestMapper.from(value)

		val wsoResponse = asyncWsoClient.getUserDetails(wsoRequest).block()

		val apiResponse = apiUserResponseMapper.from(wsoResponse!!)

		producer.sendMessage(key, apiResponse)
	}

	companion object {
		private val log: Logger = LoggerFactory.getLogger(this::class.java)
	}
}