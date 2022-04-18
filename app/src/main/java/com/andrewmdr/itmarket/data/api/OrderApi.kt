package com.andrewmdr.itmarket.data.api

import com.andrewmdr.itmarket.data.model.Order
import com.andrewmdr.itmarket.data.model.OrderBody
import retrofit2.http.*

interface OrderApi {

    @GET("/api/v1/route/get-orders")
    suspend fun getOrdersFromRoute(@Query("routeId") id: Long) : List<Order>

    @POST("/api/v1/order/take")
    suspend fun takeOrder(@Query("id") id: Long) : String

    @POST("/api/v1/order/complete")
    suspend fun completeOrder(@Query("id") id: Long) : String

    @POST("/api/v1/order/cancel")
    suspend fun closeOrder(@Query("id") id: Long) : String

    @POST("/api/v1/order/create")
    suspend fun createOrder(@Body body: OrderBody) : String

    @POST("/api/v1/route/add-order")
    suspend fun addOrderToRoute(@Query("routeId") routeId: Long, @Query("orderId") orderId: Long): String

    @DELETE("/api/v1/order/delete")
    suspend fun deleteOrder(@Query("id") id: Long): String
}