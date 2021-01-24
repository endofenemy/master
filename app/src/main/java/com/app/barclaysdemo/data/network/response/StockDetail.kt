package com.app.barclaysdemo.data.network.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


data class StockDetail(
    @SerializedName("meta")
    val meta:TimesMeta?,
    @SerializedName("values")
    val value: LinkedList<TimesValue>,
    @SerializedName("status")
    val status:String
):Serializable