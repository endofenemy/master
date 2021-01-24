package com.app.barclaysdemo.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.barclaysdemo.R
import com.app.barclaysdemo.data.db.AppDatabase
import com.app.barclaysdemo.data.network.NetworkConnectionInterceptor
import com.app.barclaysdemo.data.network.response.Stock
import com.app.barclaysdemo.data.repository.StockRepository
import com.app.barclaysdemo.ui.stock.StockModelFactory
import com.app.barclaysdemo.ui.stock.StockViewModel
import com.dlhaat.data.network.MyApi
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

class SearchStockActivity : AppCompatActivity() {
    private lateinit var stockList: RecyclerView

    private lateinit var adapter: SearchAdapter
    private val listOfStocks = LinkedList<Stock>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        val api = MyApi(networkConnectionInterceptor)
        val db = AppDatabase.invoke(this)
        val repository = StockRepository(api, db)
        val factory = StockModelFactory(repository)
        val stockViewModel = ViewModelProvider(this, factory).get(StockViewModel::class.java)
        stockViewModel.getAllStockList()
        stockViewModel.stockList.observe(this, Observer {
            progress.visibility=View.GONE
            stockList.visibility=View.VISIBLE
            adapter.updateList(it)
        })

        setupStockList()
        performSearch()
    }

    private fun performSearch() {
        val search = findViewById<EditText>(R.id.search)
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                adapter.filter.filter(s.toString())
            }

        })
    }

    private fun setupStockList() {
        stockList = findViewById<RecyclerView>(R.id.stock_list)
        stockList.layoutManager = LinearLayoutManager(this)
        adapter = SearchAdapter(this, listOfStocks)
        stockList.adapter = adapter
    }


}