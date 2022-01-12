package ru.mcb.baas.userworker.exception

class ClientRetryException(
	message: String?,
	statusCode: Int?
) : RuntimeException(message)