package com.andrewmdr.itmarket.presentation.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrewmdr.itmarket.data.model.Order
import com.andrewmdr.itmarket.data.remote.OrderRepository
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var order : Order
    val context = getApplication<Application>().applicationContext
    val repository = OrderRepository(context)

    fun takeOrder() {
        viewModelScope.launch {
            repository.takeOrder(order.id.toLong())
        }
    }

    fun completeOrder(){
        viewModelScope.launch {
            repository.completeOrder(order.id.toLong())
        }
    }

    fun closeOrder(){
        viewModelScope.launch {
            repository.closeOrder(order.id.toLong())
        }
    }

    fun deleteOrder() {
        viewModelScope.launch {
            repository.deleteOrder(order.id.toLong())
        }
    }


}