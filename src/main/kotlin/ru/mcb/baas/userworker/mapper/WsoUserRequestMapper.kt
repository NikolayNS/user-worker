package ru.mcb.baas.userworker.mapper

import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import ru.mcb.baas.userworker.model.request.ApiUserRequest
import ru.mcb.baas.userworker.model.request.WsoUserRequest

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface WsoUserRequestMapper {

	fun from(apiRequest: ApiUserRequest): WsoUserRequest
}