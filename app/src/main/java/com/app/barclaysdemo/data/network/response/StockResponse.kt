package com.app.barclaysdemo.data.network.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class StockResponse(
    @SerializedName("data")
    val data: LinkedList<Stock>
)