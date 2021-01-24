package com.app.barclaysdemo.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.app.barclaysdemo.utils.NoNetworkException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkConnectionInterceptor(
    private val context: Context,
) : Interceptor {
    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable()) {
            throw NoNetworkException("Make Sure You have Active Data Connection")
        }

        val request: Request = chain.request().newBuilder()
            .addHeader("x-rapidapi-key", "FZlVWBceAYmshMeuPNduLAYW4pLCp1Y3JF8jsnyf3ACMohTJy1")
            .addHeader("x-rapidapi-host", "twelve-data1.p.rapidapi.com")
            .build()
        return chain.proceed(request)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it != null && it.isConnected
        }
    }
}