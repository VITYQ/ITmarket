package com.andrewmdr.itmarket.data.api

import com.andrewmdr.itmarket.data.model.LoginCredentials
import com.andrewmdr.itmarket.data.model.LoginResponse
import com.andrewmdr.itmarket.data.model.UserInfo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface LoginApi {

    @POST("/api/v1/auth/login")
    suspend fun login(@Body data: LoginCredentials?): LoginResponse

    @POST("/api/v1/auth/logout")
    suspend fun logout()

    @GET("/api/v1/user/info")
    suspend fun getUserInfo(): UserInfo

}