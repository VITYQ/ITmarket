package com.andrewmdr.itmarket.data.remote

import android.content.Context
import android.util.Log
import com.andrewmdr.itmarket.data.api.LoginApi
import com.andrewmdr.itmarket.data.local.PreferencesProvider
import com.andrewmdr.itmarket.data.model.LoginCredentials
import com.andrewmdr.itmarket.data.model.LoginResponse
import com.andrewmdr.itmarket.data.model.UserInfo
import com.andrewmdr.itmarket.data.retrofit.ResourceHandler
import com.andrewmdr.itmarket.data.retrofit.ResponseHandler
import com.andrewmdr.itmarket.data.retrofit.RetrofitInstance
import retrofit2.HttpException
import retrofit2.create
import java.lang.Exception

class LoginRepository (val context: Context) {
    val api = RetrofitInstance(context).getClient().create(LoginApi::class.java)
    val responseHandler = ResponseHandler()


    suspend fun logIn(credentials: LoginCredentials): ResourceHandler<LoginResponse> {
        return try{
            val response = api.login(credentials)
            PreferencesProvider(context).setToken(response.token)
            Log.d("checklogin", "token: $response")
            return responseHandler.handleSuccess(response)
        }
        catch (e: Exception){
            return responseHandler.handleException(e)
        }

    }

    suspend fun logOut(){
        PreferencesProvider(context).logOut()
        try {
            api.logout()
        }
        catch (e: HttpException){
            Log.e("httpxce", e.toString())
        }
    }

    suspend fun getUserInfo(): ResourceHandler<UserInfo>{
        return try {
            responseHandler.handleSuccess(api.getUserInfo())
        }
        catch (e:Exception){
            responseHandler.handleException(e)
        }
    }
}