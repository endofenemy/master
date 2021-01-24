package com.app.barclaysdemo.ui.stock

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.barclaysdemo.R
import com.app.barclaysdemo.data.db.AppDatabase
import com.app.barclaysdemo.data.db.SessionManager
import com.app.barclaysdemo.data.db.entities.WatchStock
import com.app.barclaysdemo.data.network.NetworkConnectionInterceptor
import com.app.barclaysdemo.data.repository.StockRepository
import com.app.barclaysdemo.ui.search.SearchStockActivity
import com.dlhaat.data.network.MyApi
import kotlinx.android.synthetic.main.activity_stock_list.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StockListActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StockListAdapter
    val listOfStocks = ArrayList<WatchStock>()
    private lateinit var stockViewModel:StockViewModel
    private lateinit var sessionManager:SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_list)
        sessionManager= SessionManager(this)
        setupToolbar()
        setupStockList()

        val networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        val api = MyApi(networkConnectionInterceptor)
        val db = AppDatabase.invoke(this)
        val repository = StockRepository(api, db)
        val factory = StockModelFactory(repository)

        stockViewModel = ViewModelProvider(this, factory).get(StockViewModel::class.java)
        performSearch()

        date.text= SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(Date())
    }

    private fun performSearch() {
        val search = findViewById<EditText>(R.id.search)
        search.setOnClickListener {
            Intent(this, SearchStockActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun setupStockList() {
        recyclerView = findViewById<RecyclerView>(R.id.stock_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StockListAdapter(this, listOfStocks)
        recyclerView.adapter = adapter
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()
        stockViewModel.getStockToWatch(sessionManager)
        stockViewModel.watchStockList.observe(this, Observer {
            it?.let {
                listOfStocks.clear()
                listOfStocks.addAll(it)
                if (it.isNotEmpty()) {
                    recyclerView.visibility = View.VISIBLE
                    no_list.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                } else {
                    no_list.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            }

        })
    }
}