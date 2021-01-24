package com.app.barclaysdemo.data.network

import com.app.barclaysdemo.data.db.entities.WatchStock
import com.app.barclaysdemo.utils.ApiExceptions
import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun <T:Any> apiRequest(call:suspend ()-> Response<T>,) :T{

        val response=call.invoke()
        if (response.isSuccessful){
            return response.body()!!
        }else{
            val error=response.errorBody()?.string()!!
            throw ApiExceptions(error!!)
        }
    }


}