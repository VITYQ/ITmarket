package com.andrewmdr.itmarket.data.api

import com.andrewmdr.itmarket.data.model.Company
import com.andrewmdr.itmarket.data.model.Route
import retrofit2.http.*

interface CompanyApi {

    @GET("/api/v1/company/get")
    suspend fun getAllCompanies(): List<Company>

    @GET("/api/v1/company/get-routes")
    suspend fun getCompanyRoutes(@Query("companyId") companyId: Long,
    ): List<Route>

    @POST("/api/v1/route/take")
    suspend fun takeRoute(@Query("routeId") id: Long) : String

    @POST("/api/v1/route/close")
    suspend fun closeRoute(@Query("routeId") id: Long) : String

    @GET("/api/v1/user/routes")
    suspend fun getTakenRoute(): List<Route>



    @POST("/api/v1/route/create")
    suspend fun createRotue(@Body route: Route) : String


    @POST("/api/v1/company/add-route")
    suspend fun addRouteToCompany(@Query("companyId") companyId: Long,
        @Query("routeId") routeId: Long) : String


}