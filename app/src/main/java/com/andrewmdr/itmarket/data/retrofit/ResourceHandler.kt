package com.andrewmdr.itmarket.data.retrofit

data class ResourceHandler<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): ResourceHandler<T> {
            return ResourceHandler(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): ResourceHandler<T> {
            return ResourceHandler(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): ResourceHandler<T> {
            return ResourceHandler(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}