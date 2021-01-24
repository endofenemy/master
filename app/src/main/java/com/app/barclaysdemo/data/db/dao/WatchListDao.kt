package com.app.barclaysdemo.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.barclaysdemo.data.db.entities.WatchStock
import java.util.*

@Dao
interface WatchListDao {
    @Insert
    fun addStockToWatch(watchStock: WatchStock)

    @Query("SELECT * FROM WatchStock")
    fun loadAllStockToWatch(): LiveData<List<WatchStock>>?
}