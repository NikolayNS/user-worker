package ru.mcb.baas.userworker.mapper

import org.mapstruct.Mapper
import ru.mcb.baas.userworker.model.response.ApiUserResponse
import ru.mcb.baas.userworker.model.response.WsoUserResponse

@Mapper(componentModel = "spring")
interface ApiUserResponseMapper {

	fun from(apiRequest: WsoUserResponse): ApiUserResponse
}