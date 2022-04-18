package com.andrewmdr.itmarket.presentation.createOrder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.andrewmdr.itmarket.data.model.Order
import com.andrewmdr.itmarket.data.model.OrderBody
import com.andrewmdr.itmarket.data.remote.OrderRepository
import kotlinx.coroutines.launch
import org.w3c.dom.Comment
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class CreateOrderViewModel(application: Application) : AndroidViewModel(application) {

    val context = getApplication<Application>().applicationContext
    val date = MutableLiveData<LocalDateTime>()
    val orderRepository = OrderRepository(context)
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

    fun createOrder(routeId: Long, id: String, packageCount: String, address: String, house: String, service: String,
    phone: String, building: String, entrance: String, floor: String, comment: String, price: String, name: String){
        viewModelScope.launch {
            val orderBody = OrderBody(id, packageCount, address, house, service,
                "${date.value}:00.000+00:00", phone,building.toString().toInt(),
                entrance.toInt(), floor.toInt(), name, comment, price.toLong())

            orderRepository.createOrder(orderBody)
            orderRepository.addOrderToRoute(routeId, id.toLong())
            successHandler.postValue(true)
        }

    }
}