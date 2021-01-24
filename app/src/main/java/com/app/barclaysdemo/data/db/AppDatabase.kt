package com.app.barclaysdemo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.barclaysdemo.data.db.dao.WatchListDao
import com.app.barclaysdemo.data.db.entities.WatchStock

@Database(
    entities = [WatchStock::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stockWatchDao(): WatchListDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "stock.db"
            ).allowMainThreadQueries().build()
    }


}