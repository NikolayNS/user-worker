package ru.mcb.baas.userworker.model.request

import javax.validation.constraints.NotBlank

data class ApiUserRequest(
		@NotBlank
		val name: String
)
