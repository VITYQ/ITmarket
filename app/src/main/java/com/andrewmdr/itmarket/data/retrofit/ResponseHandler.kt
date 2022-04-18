package com.andrewmdr.itmarket.data.retrofit

import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.lang.Exception

enum class ErrorCodes(val code: Int) {
    SocketTimeOut(-1)
}

open class ResponseHandler {
    fun <T : Any> handleSuccess(data: T): ResourceHandler<T> {
        return ResourceHandler.success(data)
    }

    fun <T : Any> handleException(e: Exception): ResourceHandler<T> {
        return when (e) {
            is HttpException -> ResourceHandler.error(getErrorMessage(e.code()), null)
            is SocketTimeoutException -> ResourceHandler.error(getErrorMessage(ErrorCodes.SocketTimeOut.code), null)
            else -> ResourceHandler.error(getErrorMessage(Int.MAX_VALUE), null)
        }
    }

    private fun getErrorMessage(code: Int): String {
        return when (code) {
            ErrorCodes.SocketTimeOut.code -> "Timeout"
            401 -> "Unauthorised"
            404 -> "Not found"
            else -> "Something went wrong"
        }
    }
}