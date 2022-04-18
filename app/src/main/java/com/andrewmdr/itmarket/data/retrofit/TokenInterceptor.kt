package com.andrewmdr.itmarket.data.retrofit

import android.content.Context
import android.util.Log
import com.andrewmdr.itmarket.ITmarketApp
import com.andrewmdr.itmarket.MainActivity
import com.andrewmdr.itmarket.data.local.PreferencesProvider
import okhttp3.Interceptor
import okhttp3.Response
import okio.GzipSource
import java.io.IOException
import java.nio.charset.Charset
import kotlin.jvm.Throws


class TokenInterceptor(val context: Context) : Interceptor {

    fun token() : String {
        val token = PreferencesProvider(context).getToken()
        if (token.isNullOrEmpty()) {
            return ""
        }
        else{
            return token
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val token = token()

        if(!token.isNullOrEmpty()){
            Log.d("checktocekf", "'$token'")
            request = request.newBuilder()
                .addHeader("Authorization", token)
                .build()
        }
        Log.d("checktocekf", "${request.headers()}")

        val response = chain.proceed(request)

        return response

    }



}




