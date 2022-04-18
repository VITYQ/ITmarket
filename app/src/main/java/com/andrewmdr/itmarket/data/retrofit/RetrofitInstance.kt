package com.andrewmdr.itmarket.data.retrofit

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitInstance(val context: Context) {

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null) {
            val client = OkHttpClient.Builder()
                .addInterceptor(TokenInterceptor(context))
                .build()

            retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl("http://10.0.2.2:7777")
                .addConverterFactory(ScalarsConverterFactory.create()) // позволяет принимать response body в виде String
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit!!
    }
}