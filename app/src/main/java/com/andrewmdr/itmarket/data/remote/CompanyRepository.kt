package com.andrewmdr.itmarket.data.remote

import android.content.Context
import android.util.Log
import com.andrewmdr.itmarket.data.api.CompanyApi
import com.andrewmdr.itmarket.data.api.LoginApi
import com.andrewmdr.itmarket.data.local.PreferencesProvider
import com.andrewmdr.itmarket.data.model.Company
import com.andrewmdr.itmarket.data.model.Route
import com.andrewmdr.itmarket.data.retrofit.ResourceHandler
import com.andrewmdr.itmarket.data.retrofit.ResponseHandler
import com.andrewmdr.itmarket.data.retrofit.RetrofitInstance
import java.lang.Exception

class CompanyRepository(val context: Context) {
    val api = RetrofitInstance(context).getClient().create(CompanyApi::class.java)
    val responseHandler = ResponseHandler()

    suspend fun getCompaniesList(): ResourceHandler<List<Company>> {
        return try {
            Log.d("checkconn", "started companies")
            val response = api.getAllCompanies()
            Log.d("checkconn", "${response}")
            return responseHandler.handleSuccess(response)
        }
        catch (e:Exception){
            Log.d("checkconn", "error ${e.localizedMessage}")
            return responseHandler.handleException(e)
        }
    }

    suspend fun getRoutesList(companyId: Long): ResourceHandler<List<Route>> {
        return try {
            Log.d("checkconn", "started")
            responseHandler.handleSuccess(api.getCompanyRoutes(companyId))
        }
        catch (e: Exception){
            Log.d("checkconn", "error ${e.localizedMessage}")
            responseHandler.handleException(e)
        }
    }

    suspend fun createRoute(route: Route) : ResourceHandler<String> {
        return try {
            responseHandler.handleSuccess(api.createRotue(route))
        }
        catch (e: Exception){
            responseHandler.handleException(e)
        }
    }

    suspend fun addRouteToCompany(companyId: Long, routeId: Long) : ResourceHandler<String> {
        return try {
            responseHandler.handleSuccess(api.addRouteToCompany(companyId, routeId))
        }
        catch (e:Exception){
            responseHandler.handleException(e)
        }
    }

}