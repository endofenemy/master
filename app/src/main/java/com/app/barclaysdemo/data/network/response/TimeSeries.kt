package com.app.barclaysdemo.data.network.response

import java.io.Serializable

data class TimeSeries(
    val result: Map<String, StockDetail>? = null
):Serializable