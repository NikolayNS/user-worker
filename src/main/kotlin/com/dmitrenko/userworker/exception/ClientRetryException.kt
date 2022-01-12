package com.dmitrenko.userworker.exception

class ClientRetryException(
	message: String?,
	statusCode: Int?
) : RuntimeException(message)