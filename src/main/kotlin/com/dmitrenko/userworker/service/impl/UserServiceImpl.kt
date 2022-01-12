package com.dmitrenko.userworker.service.impl

import com.dmitrenko.userworker.client.AsyncWsoClient
import com.dmitrenko.userworker.kafka.producer.Producer
import com.dmitrenko.userworker.mapper.ApiUserResponseMapper
import com.dmitrenko.userworker.mapper.WsoUserRequestMapper
import com.dmitrenko.userworker.model.request.ApiUserRequest
import com.dmitrenko.userworker.model.response.ApiUserResponse
import com.dmitrenko.userworker.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

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