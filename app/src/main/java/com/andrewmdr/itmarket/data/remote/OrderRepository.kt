package com.andrewmdr.itmarket.data.remote

import android.content.Context
import com.andrewmdr.itmarket.data.api.CompanyApi
import com.andrewmdr.itmarket.data.api.OrderApi
import com.andrewmdr.itmarket.data.model.Order
import com.andrewmdr.itmarket.data.model.OrderBody
import com.andrewmdr.itmarket.data.model.Route
import com.andrewmdr.itmarket.data.retrofit.ResourceHandler
import com.andrewmdr.itmarket.data.retrofit.ResponseHandler
import com.andrewmdr.itmarket.data.retrofit.RetrofitInstance
import java.lang.Exception

class OrderRepository(val context: Context) {
    val api = RetrofitInstance(context).getClient().create(OrderApi::class.java)
    val apiCompany = RetrofitInstance(context).getClient().create(CompanyApi::class.java)
    val responseHandler = ResponseHandler()

    suspend fun fetchOrders(id: Long) : ResourceHandler<List<Order>>{
        return try {
            responseHandler.handleSuccess(api.getOrdersFromRoute(id))
        }
        catch (e: Exception){
            responseHandler.handleException(e)
        }
    }

    suspend fun getTakenRoute(): ResourceHandler<List<Route>> {
        return try {
            responseHandler.handleSuccess(apiCompany.getTakenRoute())
        }
        catch (e: Exception){
            responseHandler.handleException(e)
        }
    }

    suspend fun takeRoute(id: Long): ResourceHandler<String>{
        return try {
            responseHandler.handleSuccess(apiCompany.takeRoute(id))
        }
        catch (e: Exception){
            responseHandler.handleException(e)
        }
    }

    suspend fun closeRoute(id: Long): ResourceHandler<String>{
        return try {
            responseHandler.handleSuccess(apiCompany.closeRoute(id))
        }
        catch (e: Exception){
            responseHandler.handleException(e)
        }
    }

    suspend fun takeOrder(id: Long) : ResourceHandler<String>{
        return try {
            responseHandler.handleSuccess(api.takeOrder(id))
        }
        catch (e:Exception){
            responseHandler.handleException(e)
        }
    }


    suspend fun completeOrder(id: Long) : ResourceHandler<String>{
        return try {
            responseHandler.handleSuccess(api.completeOrder(id))
        }
        catch (e:Exception){
            responseHandler.handleException(e)
        }
    }


    suspend fun closeOrder(id: Long) : ResourceHandler<String>{
        return try {
            responseHandler.handleSuccess(api.closeOrder(id))
        }
        catch (e:Exception){
            responseHandler.handleException(e)
        }
    }

    suspend fun deleteOrder(id: Long) : ResourceHandler<String>{
        return try {
            responseHandler.handleSuccess(api.deleteOrder(id))
        }
        catch (e:Exception){
            responseHandler.handleException(e)
        }
    }


    suspend fun createOrder(body: OrderBody) : ResourceHandler<String>{
        return try {
            responseHandler.handleSuccess(api.createOrder(body))
        }
        catch (e:Exception){
            responseHandler.handleException(e)
        }
    }

    suspend fun addOrderToRoute(routeId: Long, orderId: Long) : ResourceHandler<String>{
        return try {
            responseHandler.handleSuccess(api.addOrderToRoute(routeId, orderId))
        }
        catch (e:Exception){
            responseHandler.handleException(e)
        }
    }


}