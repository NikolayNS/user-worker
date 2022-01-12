package com.dmitrenko.userworker.mapper

import com.dmitrenko.userworker.model.request.ApiUserRequest
import com.dmitrenko.userworker.model.request.WsoUserRequest
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface WsoUserRequestMapper {

	fun from(apiRequest: ApiUserRequest): WsoUserRequest
}