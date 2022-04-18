package com.andrewmdr.itmarket.presentation.route

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrewmdr.itmarket.data.api.CompanyApi
import com.andrewmdr.itmarket.data.model.Company
import com.andrewmdr.itmarket.data.model.Route
import com.andrewmdr.itmarket.data.model.UserInfo
import com.andrewmdr.itmarket.data.remote.CompanyRepository
import com.andrewmdr.itmarket.data.remote.LoginRepository
import com.andrewmdr.itmarket.data.retrofit.ResourceHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RouteViewModel(application: Application) : AndroidViewModel(application) {

    val companiesLiveData = MutableLiveData<ResourceHandler<List<Company>>>()
    val routesLiveData = MutableLiveData<ResourceHandler<List<Route>>>()
    val userLiveData = MutableLiveData<ResourceHandler<UserInfo>>()
    val context = getApplication<Application>().applicationContext
    val repository = CompanyRepository(context)
    val loginRepository = LoginRepository(context)
    val isLoading = MutableLiveData<Boolean>(false)
    var currentId : Long = 0

    init {
        fetchCompanies()
    }

    fun fetchUserInfo() {
        viewModelScope.launch {
            isLoading.postValue(true)
            userLiveData.postValue(loginRepository.getUserInfo())
            isLoading.postValue(false)
        }
    }

    fun fetchRoutes(id: Long ) {
        viewModelScope.launch {
            currentId = id
            isLoading.postValue(true)
            routesLiveData.postValue(repository.getRoutesList(id))
            isLoading.postValue(false)
        }

    }

    private fun fetchCompanies() {
        viewModelScope.launch {
            isLoading.postValue(true)
            companiesLiveData.postValue(repository.getCompaniesList())
            isLoading.postValue(false)
        }
    }
}