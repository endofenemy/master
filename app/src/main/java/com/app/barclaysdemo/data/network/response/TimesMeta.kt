package com.app.barclaysdemo.data.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class TimesMeta(
    @SerializedName("symbol")
    val symbol:String,
    @SerializedName("interval")
    val interval:String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("exchange")
    val exchange:String,
    @SerializedName("exchange_timezone")
    val exchangeTimeZone: String,
    @SerializedName("type")
    val type:String
):Serializable
