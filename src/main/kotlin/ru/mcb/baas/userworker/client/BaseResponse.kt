package ru.mcb.baas.userworker.client

data class BaseResponse<R>(
		private val response: R?,
		private val exception: RuntimeException?
)