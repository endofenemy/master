package com.app.barclaysdemo.ui.search

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.barclaysdemo.R
import com.app.barclaysdemo.data.db.AppDatabase
import com.app.barclaysdemo.data.db.SessionManager
import com.app.barclaysdemo.data.db.dao.WatchListDao
import com.app.barclaysdemo.data.db.entities.WatchStock
import com.app.barclaysdemo.data.network.NetworkConnectionInterceptor
import com.app.barclaysdemo.data.network.response.Stock
import com.app.barclaysdemo.data.network.response.StockDetail
import com.app.barclaysdemo.data.repository.StockRepository
import com.app.barclaysdemo.ui.stock.StockModelFactory
import com.app.barclaysdemo.ui.stock.StockViewModel
import com.app.barclaysdemo.utils.Constants
import com.app.barclaysdemo.utils.Utils
import com.dlhaat.data.network.MyApi
import com.google.android.material.button.MaterialButton
import com.yabu.livechart.model.DataPoint
import com.yabu.livechart.view.LiveChart
import kotlinx.android.synthetic.main.activity_stock_details.*
import java.text.DecimalFormat

class StockDetailActivity : AppCompatActivity(), View.OnClickListener,
    DurationAdapter.OnDurationClick {
    private var r: Double = 0.0
    private lateinit var stockViewModel: StockViewModel
    private var stockDetail: StockDetail? = null
    private lateinit var stock: Stock
    val defaultInterval: String = "1day"
    lateinit var stockWatchDao: WatchListDao
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_details)
        sessionManager = SessionManager(this)
        val intent: Intent? = intent
        intent?.let {
            stock = intent.getSerializableExtra(Constants.stock) as Stock
            getStockDetails(stock)

        }
        setupDurationList()
    }

    private fun init(stockDetail: StockDetail?) {
        val symbol: TextView = findViewById(R.id.symbol)
        val name: TextView = findViewById(R.id.name)
        val atOpen: TextView = findViewById(R.id.at_open)
        val atClose: TextView = findViewById(R.id.at_close)
        val rate: TextView = findViewById(R.id.rate)
        val openValue: TextView = findViewById(R.id.open_value)
        val closeValue: TextView = findViewById(R.id.close_value)
        val highValue: TextView = findViewById(R.id.high_value)
        val lowValue: TextView = findViewById(R.id.low_value)
        val volume: TextView = findViewById(R.id.volume_value)
        val addToWatchList: MaterialButton = findViewById(R.id.add_to_watch_list)
        addToWatchList.setOnClickListener(this)
        addToWatchList.visibility =
            if (sessionManager.addVisibility(stock.symbol, stock.name)) View.VISIBLE else View.GONE
        symbol.text = stock.symbol
        name.text = stock.name
        atOpen.text = stockDetail?.value?.get(0)?.open?.let { getDisplayString(it) }
        atClose.text = stockDetail?.value?.get(0)?.close?.let { getDisplayString(it) }
        setRate(rate, stockDetail?.value?.get(0)?.open, stockDetail?.value?.get(0)?.close)
        openValue.text = stockDetail?.value?.get(0)?.open?.let { getDisplayString(it) }
        closeValue.text = stockDetail?.value?.get(0)?.close?.let { getDisplayString(it) }
        highValue.text = stockDetail?.value?.get(0)?.high?.let { getDisplayString(it) }
        lowValue.text = stockDetail?.value?.get(0)?.low?.let { getDisplayString(it) }
        volume.text = stockDetail?.value?.get(0)?.volume?.let { getDisplayString(it) }

        setupLiveChart()
    }

    private fun handleAddVisibility(addVisibility: Boolean) {

    }

    private fun getDisplayString(value: String): String {
        val format = DecimalFormat("##.##")
        return format.format(value.toDouble())
    }

    private fun setRate(rate: TextView, open: String?, close: String?) {
        val openValue = open?.toDouble()
        val closeValue = close?.toDouble()
        openValue?.let {
            r = (closeValue!! - openValue!!) * (100 / openValue!!)
            Log.e("Rate :: ", r.toString())
            if (closeValue!! > openValue!!) {
                rate.text = "+${String.format("%.02f", r)}%"
                rate.setTextColor(Color.GREEN)
            } else {
                rate.text = "${String.format("%.02f", r)}%"
                rate.setTextColor(Color.RED)
            }
        }
    }

    private fun getStockDetails(stock: Stock) {
        val networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        val api = MyApi(networkConnectionInterceptor)
        val db = AppDatabase.invoke(this)
        stockWatchDao = db.stockWatchDao()
        val repository = StockRepository(api, db)
        val factory = StockModelFactory(repository)
        stockViewModel = ViewModelProvider(this, factory).get(StockViewModel::class.java)
        stockViewModel.getStockDetails(stock.symbol, defaultInterval)
        stockViewModel.stockDetail.observe(this, Observer {
            stockDetail = it
            progress.visibility = View.GONE
            content.visibility = View.VISIBLE
            setData(stockDetail)
        })
    }

    private fun setData(stockDetail: StockDetail?) {
        init(stockDetail)
    }

    private fun setupLiveChart() {
        val liveChart: LiveChart = findViewById(R.id.live_chart)
        val dataset = Utils.getActualDataSet(stockDetail?.value)

        val liveChartStyle = Utils.getLiveChartStyle(r > 0)

        liveChart.setDataset(dataset)
            .drawYBounds()
            .drawStraightPath()
            .setLiveChartStyle(liveChartStyle)
            .drawBaseline()
            .drawFill(withGradient = true)
            .drawLastPointLabel()
            .drawLastPointLabel()
            .setOnTouchCallbackListener(
                object : LiveChart.OnTouchCallback {
                    override fun onTouchCallback(point: DataPoint) {
                        Log.e(
                            "Data Points",
                            "(${"%.2f".format(point.x)}, ${"%.2f".format(point.y)})"
                        )
                    }

                    override fun onTouchFinished() {
                    }
                }
            )
            .drawDataset()

    }

    private fun setupDurationList() {
        val durationList = findViewById<RecyclerView>(R.id.duration_list)
        durationList.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val adapter = DurationAdapter(this, mutableListOf("1h", "2h", "1D", "1W", "1M"))
        adapter.setDurationClickListener(this)
        durationList.adapter = adapter
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.add_to_watch_list) {
            val watchStock = WatchStock(
                id = System.currentTimeMillis(),
                symbol = stock.symbol,
                name = stock.name,
                high = (stockDetail?.value?.get(0)?.high),
                low = (stockDetail?.value?.get(0)?.low),
                value = (stockDetail?.value?.get(0)?.open),
                close = (stockDetail?.value?.get(0)?.close)
            )
            stockViewModel.addStockToWatch(watchStock, sessionManager)
        }
    }

    override fun onDurationClick(interval: String) {
        progress.visibility = View.VISIBLE
        stockViewModel.getStockDetails(stock.symbol, getInterval(interval))
    }

    private fun getInterval(interval: String): String {
        return when {
            interval.equals("1h", true) -> {
                "1h"
            }
            interval.equals("2h", true) -> {
                "2h"
            }
            interval.equals("1D", true) -> {
                "1day"
            }
            interval.equals("1W", true) -> {
                "1week"
            }
            interval.equals("1M", true) -> {
                "1month"
            }
            else -> {
                defaultInterval
            }
        }
    }
}