package com.andrewmdr.itmarket.presentation.createRoute

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.andrewmdr.itmarket.data.model.Route
import com.andrewmdr.itmarket.data.remote.CompanyRepository
import com.andrewmdr.itmarket.data.remote.LoginRepository
import com.andrewmdr.itmarket.data.remote.OrderRepository
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import kotlin.time.Duration.Companion.minutes

class CreateRouteViewModel(application: Application) : AndroidViewModel(application) {

    val context = getApplication<Application>().applicationContext
    val date = MutableLiveData<LocalDateTime>()
    val companyRepository = CompanyRepository(context)
    val userRepository = LoginRepository(context)

    val successHandler = MutableLiveData<Boolean>()



    fun setTime(hours: Int, minutes: Int){
        val tmp = date.value
        if(date.value==null){
            date.value = LocalDateTime.now().plusHours(hours.toLong()).plusMinutes(minutes.toLong())
        }
        else{
            date.value = date.value!!.plusHours(hours.toLong()).plusMinutes(minutes.toLong())
        }
    }

    fun setDate(timestamp: Long){
        date.value = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId())
//        date.value = LocalDateTime(timestamp)
    }

    fun createRoute(routeId: String, address: String) {
        viewModelScope.launch {
            val user = userRepository.getUserInfo()
            val companyId = user.data?.companyDTO?.id
            val route = Route(routeId.toLong(), "${date.value}:00.000+00:00", address)
            companyRepository.createRoute(route)
            companyRepository.addRouteToCompany(companyId!!, routeId.toLong())
            successHandler.postValue(true)
//            repository.createRoute()
//            repository.addRouteToCompany()
        }
    }
}