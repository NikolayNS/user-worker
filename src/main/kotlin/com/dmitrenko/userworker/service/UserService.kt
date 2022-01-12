package com.dmitrenko.userworker.service

import com.dmitrenko.userworker.model.request.ApiUserRequest

interface UserService {
	
	fun collectUserDetails(key: String, value: ApiUserRequest)
}