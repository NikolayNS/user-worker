package com.dmitrenko.userworker.mapper

import com.dmitrenko.userworker.model.response.ApiUserResponse
import com.dmitrenko.userworker.model.response.WsoUserResponse
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ApiUserResponseMapper {

	fun from(apiRequest: WsoUserResponse): ApiUserResponse
}