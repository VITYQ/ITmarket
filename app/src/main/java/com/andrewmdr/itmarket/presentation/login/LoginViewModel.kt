package com.andrewmdr.itmarket.presentation.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andrewmdr.itmarket.data.model.LoginCredentials
import com.andrewmdr.itmarket.data.model.LoginResponse
import com.andrewmdr.itmarket.data.remote.LoginRepository
import com.andrewmdr.itmarket.data.retrofit.ResourceHandler
import com.andrewmdr.itmarket.data.retrofit.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val context = getApplication<Application>().applicationContext

    val loginHandler = MutableLiveData<ResourceHandler<LoginResponse>>()

    fun login(login: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val credentials = LoginCredentials(login, password)
            loginHandler.postValue(LoginRepository(context).logIn(credentials))
        }
    }

    fun clearData() {
        loginHandler.value = ResourceHandler(Status.SUCCESS, LoginResponse("", ""), "")
    }
}