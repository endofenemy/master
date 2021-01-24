package com.app.barclaysdemo.ui.stock

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.barclaysdemo.data.db.SessionManager
import com.app.barclaysdemo.data.db.entities.WatchStock
import com.app.barclaysdemo.data.network.response.Stock
import com.app.barclaysdemo.data.network.response.StockDetail
import com.app.barclaysdemo.data.network.response.StockResponse
import com.app.barclaysdemo.data.repository.StockRepository
import com.app.barclaysdemo.utils.ApiExceptions
import com.app.barclaysdemo.utils.Coroutines
import com.app.barclaysdemo.utils.NoNetworkException
import com.google.gson.Gson
import java.util.*

class StockViewModel(
    private val repository: StockRepository
) : ViewModel() {
    // This is used for Stock list
    private val _stockList = MutableLiveData<LinkedList<Stock>>()
    val stockList: LiveData<LinkedList<Stock>>
        get() = _stockList

    // This is used for stock details
    private val _stockDetail = MutableLiveData<StockDetail>()
    val stockDetail: LiveData<StockDetail>
        get() = _stockDetail

    private val _watchStockList = MutableLiveData<List<WatchStock>>()
    val watchStockList: LiveData<List<WatchStock>>
        get() = _watchStockList

    fun getStockDetails(symbol: String, interval: String) {
        Coroutines.main {
            try {
                val response: StockDetail = repository.getStockDetails(symbol, interval)
                Log.e("Time Series Response : ", Gson().toJson(response))
                _stockDetail.postValue(response)
            } catch (e: ApiExceptions) {
                e.printStackTrace()
            } catch (e: NoNetworkException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAllStockList() {
        Coroutines.main {
            try {
                val response: StockResponse = repository.getStockList()
                Log.e("Stock Response : ", Gson().toJson(response))
                _stockList.postValue(response.data)
            } catch (e: ApiExceptions) {
                e.printStackTrace()
            } catch (e: NoNetworkException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addStockToWatch(watchStock: WatchStock, sessionManager: SessionManager) {
        repository.addStockToWatch(watchStock, sessionManager)

    }

    fun getStockToWatch(sessionManager: SessionManager) {
        val response: List<WatchStock>? = repository.getStockToWatch(sessionManager)
        response?.let {
            Log.e("Watch Stock Response ", Gson().toJson(response))
            _watchStockList.postValue(response)
        }

    }


}