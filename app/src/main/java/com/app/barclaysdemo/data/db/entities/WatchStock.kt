package com.app.barclaysdemo.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "WatchStock")
data class WatchStock(
    @PrimaryKey(autoGenerate = false)
    val id: Long? = null,
    val symbol: String?,
    val name: String?,
    val value: String?,
    val high: String?,
    val low: String?,
    val close:String?
) : Serializable