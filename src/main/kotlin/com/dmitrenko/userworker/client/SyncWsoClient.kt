package com.dmitrenko.userworker.client

import com.dmitrenko.userworker.model.request.WsoUserRequest
import com.dmitrenko.userworker.model.response.WsoUserResponse

interface SyncWsoClient {

	fun getUserDetails(request: WsoUserRequest): BaseResponse<WsoUserResponse>
}