package com.app.barclaysdemo.ui.stock

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.barclaysdemo.R
import com.app.barclaysdemo.data.db.entities.WatchStock
import com.app.barclaysdemo.data.network.response.Stock
import com.app.barclaysdemo.ui.search.StockDetailActivity
import com.app.barclaysdemo.utils.Constants
import com.app.barclaysdemo.utils.Utils
import com.yabu.livechart.view.LiveChart

class StockListAdapter(
    private val context: Context,
    val listOfStocks: ArrayList<WatchStock>
) : RecyclerView.Adapter<StockListAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val liveChart: LiveChart = itemView.findViewById(R.id.live_chart)
        val root: ConstraintLayout = itemView.findViewById(R.id.root)
        val symbol: TextView = itemView.findViewById(R.id.symbol)
        val name: TextView = itemView.findViewById(R.id.name)
        val price: TextView = itemView.findViewById(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.item_stock_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val watchStock = listOfStocks[position]
        holder.symbol.text = watchStock.symbol
        holder.name.text = watchStock.name
        holder.price.text = Utils.getDisplayString(watchStock.value!!)
        holder.root.setOnClickListener {
            val stock=Stock("","","",watchStock.name!!,watchStock.symbol!!,"")
            Intent(context, StockDetailActivity::class.java).apply {
                this.putExtra(Constants.stock,stock)
                context.startActivity(this)
            }
        }
        val dataset = Utils.getDataSet()
        val style =
            Utils.getLiveChartStyle(watchStock.close?.toDouble()!! > watchStock.value?.toDouble()!!)
        holder.liveChart.setDataset(dataset)
            .setLiveChartStyle(style)
            .drawBaseline()
            .drawFill(withGradient = true)
            .drawLastPointLabel()
            .drawDataset()
    }

    override fun getItemCount(): Int = listOfStocks.size
}