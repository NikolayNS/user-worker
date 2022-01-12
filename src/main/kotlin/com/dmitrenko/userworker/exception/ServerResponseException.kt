package com.dmitrenko.userworker.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

class ServerResponseException(
	httpStatus: HttpStatus,
	httpHeaders: HttpHeaders,
	bodyErrorResponse: String
) : RuntimeException() {

	init {
		log.error("Error ClientResponseException. ErrorResponse={} HttpStatus = {} HttpHeaders = {}", bodyErrorResponse, httpStatus, httpHeaders)
	}

	companion object {
		private val log: Logger = LoggerFactory.getLogger(this::class.java)
	}
}