package ru.mcb.baas.userworker.service

import ru.mcb.baas.userworker.model.request.ApiUserRequest

interface UserService {
	
	fun collectUserDetails(key: String, value: ApiUserRequest)
}