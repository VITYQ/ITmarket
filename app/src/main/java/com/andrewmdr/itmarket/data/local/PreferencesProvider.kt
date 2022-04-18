package com.andrewmdr.itmarket.data.local

import android.content.Context
import android.preference.PreferenceManager

class PreferencesProvider(val context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun setToken(token: String) {
        preferences.edit()
            .putString("token", token)
            .apply()
    }

    fun getToken():  String {
        return preferences.getString("token", "")!!
    }

    fun logOut() {
        preferences.edit()
            .clear()
            .apply()
    }
}