package com.andrewmdr.itmarket

import android.app.Application
import android.content.Context
import android.util.Log

class ITmarketApp : Application() {
    private var instance: ITmarketApp? = null

    fun getInstance(): ITmarketApp? {
        return instance
    }

    fun getContext(): Context? {
        return instance
    }

    override fun onCreate() {
        instance = this
        Log.d("checkapp", "launched")
        super.onCreate()
    }
}