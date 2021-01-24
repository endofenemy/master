package com.app.barclaysdemo.data.db

import android.content.Context
import android.content.SharedPreferences
import com.app.barclaysdemo.data.db.entities.WatchStock
import com.app.barclaysdemo.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SessionManager(
    val context: Context
) {
    private val PREF_NAME = "DL_HAAT"
    private val PRIVATE_MODE = 0

    private val pref: SharedPreferences? = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private val editor: SharedPreferences.Editor? = pref?.edit()

    fun clearData() {
        editor!!.clear()
        editor.commit()
    }

    fun saveWatch(watchStock: WatchStock) {
        var list: ArrayList<WatchStock>? = getWatch()
        if (list.isNullOrEmpty()) {
            list = ArrayList()
        }
        list.add(watchStock)
        val json = Gson().toJson(list)
        editor?.let {
            it.putString(Constants.WATCH_LIST, json)
            it.commit()
        }
    }


    fun getWatch(): ArrayList<WatchStock>? {
        val s = pref?.getString(Constants.WATCH_LIST, "")
        val type: Type = object : TypeToken<List<WatchStock?>?>() {}.type
        return Gson().fromJson(s, type)
    }

    fun addVisibility(symbol: String, name: String): Boolean {
        val list = getWatch()
        var isVisible = true
        list?.let {
            for (watchStock in it) {
                if (watchStock.symbol.equals(symbol, true) && watchStock.name.equals(name, true)) {
                    isVisible = false
                    return@let
                }
            }
        }
        return isVisible
    }


}