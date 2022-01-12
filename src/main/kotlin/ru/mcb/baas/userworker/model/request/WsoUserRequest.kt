package ru.mcb.baas.userworker.model.request

import javax.validation.constraints.NotBlank

data class WsoUserRequest(
		@NotBlank
		val name: String
)