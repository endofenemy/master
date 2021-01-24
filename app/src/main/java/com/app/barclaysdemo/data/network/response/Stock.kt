package com.app.barclaysdemo.data.network.response

import java.io.Serializable

data class Stock(
    val country: String,
    val currency: String,
    val exchange: String,
    val name: String,
    val symbol: String,
    val type: String
) : Serializable