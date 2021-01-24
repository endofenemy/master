package com.app.barclaysdemo.data.repository

import com.app.barclaysdemo.data.db.AppDatabase
import com.app.barclaysdemo.data.db.SessionManager
import com.app.barclaysdemo.data.db.entities.WatchStock
import com.app.barclaysdemo.data.network.SafeApiRequest
import com.app.barclaysdemo.data.network.response.StockDetail
import com.app.barclaysdemo.data.network.response.StockResponse
import com.dlhaat.data.network.MyApi

class StockRepository(
    val api: MyApi,
    val db: AppDatabase
) : SafeApiRequest() {
    suspend fun getStockDetails(symbol: String, interval: String): StockDetail {
        return apiRequest { api.getStockDetails(symbol, interval, "json", 30) }
    }

    suspend fun getStockList(): StockResponse {
        return apiRequest { api.getStockList("NASDAQ", "json") }
    }

    fun addStockToWatch(watchStock: WatchStock, sessionManager: SessionManager) {
//        db.stockWatchDao().addStockToWatch(watchStock)
        sessionManager.saveWatch(watchStock)
    }


    fun getStockToWatch(sessionManager: SessionManager)= sessionManager.getWatch()
//        db.stockWatchDao().loadAllStockToWatch()
}