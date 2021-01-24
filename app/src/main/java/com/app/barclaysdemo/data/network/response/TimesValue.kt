package com.app.barclaysdemo.data.network.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TimesValue(
    @SerializedName("datetime")
    val dateTime:String,
    @SerializedName("open")
    val open:String,
    @SerializedName("high")
    val high:String,
    @SerializedName("low")
    val low:String,
    @SerializedName("close")
    val close:String,
    @SerializedName("volume")
    val volume:String
):Serializable
