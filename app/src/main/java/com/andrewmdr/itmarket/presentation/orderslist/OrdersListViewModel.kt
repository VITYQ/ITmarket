package com.andrewmdr.itmarket.presentation.orderslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrewmdr.itmarket.data.model.Order
import com.andrewmdr.itmarket.data.remote.CompanyRepository
import com.andrewmdr.itmarket.data.remote.OrderRepository
import com.andrewmdr.itmarket.data.retrofit.ResourceHandler
import com.andrewmdr.itmarket.data.retrofit.Status
import kotlinx.coroutines.launch

class OrdersListViewModel(application: Application) : AndroidViewModel(application) {

    val ordersLiveData = MutableLiveData<ResourceHandler<List<Order>>?>()

    val context = getApplication<Application>().applicationContext
    val repository = OrderRepository(context)
    val startPoint = MutableLiveData("")
    val routeId = MutableLiveData<Long>()
    val errorHandler = MutableLiveData(false)
    val routeStatus = MutableLiveData<RouteStatus>(RouteStatus.EMPTY_SCREEN)

    fun fetchOrdersList(id: Long, fromDrawer: Boolean){
        viewModelScope.launch {
            routeStatus.postValue(RouteStatus.LOADING)
            routeId.postValue(id)
            ordersLiveData.postValue(repository.fetchOrders(id))
            if (fromDrawer) routeStatus.postValue(RouteStatus.IS_TAKEN)
            else routeStatus.postValue(RouteStatus.CREATED)


        }
    }

    fun fetchCurrentRoute() {
        viewModelScope.launch {
            routeStatus.postValue(RouteStatus.LOADING)
            val response = repository.getTakenRoute()
            if (response.status == Status.ERROR) errorHandler.postValue(true)
            if(response.data.isNullOrEmpty()){
                routeStatus.postValue(RouteStatus.EMPTY_SCREEN)
            }
            else{
                fetchOrdersList(response.data[0].id, true)
                routeStatus.postValue(RouteStatus.IS_TAKEN)
                routeId.postValue(response.data[0].id)
                startPoint.postValue(response.data[0].address)
            }
        }

    }

    fun takeRoute(id: Long) {
        viewModelScope.launch {
            routeStatus.postValue(RouteStatus.LOADING)
            repository.takeRoute(id)
            fetchCurrentRoute()
            routeStatus.postValue(RouteStatus.IS_TAKEN)
        }
    }

    fun closeRoute(id: Long) {
        viewModelScope.launch {
            routeStatus.postValue(RouteStatus.LOADING)
            repository.closeRoute(id)
            routeStatus.postValue(RouteStatus.EMPTY_SCREEN)
        }
    }




}
enum class RouteStatus{
    IS_TAKEN,
    CREATED,
    EMPTY_SCREEN,
    LOADING
}