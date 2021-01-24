package com.dlhaat.data.network


import com.app.barclaysdemo.data.network.NetworkConnectionInterceptor
import com.app.barclaysdemo.data.network.response.StockDetail
import com.app.barclaysdemo.data.network.response.StockResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MyApi {
    @GET("time_series")
    suspend fun getStockDetails(
        @Query("symbol") symbols: String,
        @Query("interval") interval: String,
        @Query("format") format: String,
        @Query("outputsize") size: Int
    ): Response<StockDetail>

    @GET("stocks")
    suspend fun getStockList(
        @Query("exchange") exchange: String,
        @Query("format") format: String
    ): Response<StockResponse>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyApi {

            val BASE_URL = "https://twelve-data1.p.rapidapi.com/"
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}