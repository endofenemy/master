package com.app.barclaysdemo.utils

import android.graphics.Color
import com.app.barclaysdemo.data.network.response.TimesValue
import com.yabu.livechart.model.DataPoint
import com.yabu.livechart.model.Dataset
import com.yabu.livechart.view.LiveChartStyle
import java.text.DecimalFormat
import java.util.*

class Utils {
    companion object {
        fun getDataSet(): Dataset {
            return Dataset(
                mutableListOf(
                    DataPoint(1.0f, 100.0f),
                    DataPoint(2.0f, 200.0f),
                    DataPoint(3.0f, 300.0f),
                    DataPoint(4.0f, 250.0f),
                    DataPoint(5.0f, 450.0f),
                    DataPoint(6.0f, 350.0f),
                    DataPoint(7.0f, 300.0f),
                    DataPoint(8.0f, 320.0f),
                    DataPoint(9.0f, 290.0f),
                    DataPoint(10.0f, 100.0f),
                    DataPoint(11.0f, 200.0f),
                    DataPoint(12.0f, 200.0f),
                    DataPoint(13.0f, 300.0f),
                    DataPoint(14.0f, 250.0f),
                    DataPoint(15.0f, 450.0f),
                    DataPoint(16.0f, 350.0f),
                    DataPoint(17.0f, 300.0f),
                    DataPoint(18.0f, 320.0f),
                    DataPoint(19.0f, 290.0f),
                )
            )
        }

        fun getLiveChartStyle(isPositive: Boolean): LiveChartStyle {
            if (isPositive) {
                return LiveChartStyle().apply {
                    textColor = Color.WHITE
                    textHeight = 30f
                    mainColor = Color.GREEN
                    mainFillColor = Color.GREEN
                    baselineColor = Color.GRAY
                    pathStrokeWidth = 6f
                    baselineStrokeWidth = 6f
                    positiveColor = Color.GREEN
                    negativeColor = Color.RED
                }
            } else {
                return LiveChartStyle().apply {
                    textColor = Color.WHITE
                    textHeight = 30f
                    mainColor = Color.RED
                    mainFillColor = Color.RED
                    baselineColor = Color.GRAY
                    pathStrokeWidth = 6f
                    baselineStrokeWidth = 6f
                    positiveColor = Color.GREEN
                    negativeColor = Color.RED
                }
            }
        }

        fun getActualDataSet(valueList: LinkedList<TimesValue>?): Dataset {
            val list = mutableListOf<DataPoint>()
            for (i in 1 until valueList!!.size) {
                val d = DataPoint(i.toFloat(), valueList?.get(i).open.toFloat())
                list.add(d)
            }
            return Dataset(list)
        }

         fun getDisplayString(value: String): String {
            val format = DecimalFormat("##.##")
            return format.format(value.toDouble())
        }
    }
}