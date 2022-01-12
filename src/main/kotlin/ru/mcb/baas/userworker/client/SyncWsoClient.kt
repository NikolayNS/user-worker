package ru.mcb.baas.userworker.client

import ru.mcb.baas.userworker.model.request.WsoUserRequest
import ru.mcb.baas.userworker.model.response.WsoUserResponse

interface SyncWsoClient {

	fun getUserDetails(request: WsoUserRequest): BaseResponse<WsoUserResponse>
}