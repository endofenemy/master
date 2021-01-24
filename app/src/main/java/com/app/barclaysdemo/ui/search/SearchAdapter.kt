package com.app.barclaysdemo.ui.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.barclaysdemo.R
import com.app.barclaysdemo.data.network.response.Stock
import com.app.barclaysdemo.utils.Constants
import java.util.*

class SearchAdapter(
    private val context: Context,
    private val listOfStocks: LinkedList<Stock>
) : RecyclerView.Adapter<SearchAdapter.Holder>(), Filterable {
    var filterList = LinkedList<Stock>()

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val symbol: TextView = itemView.findViewById(R.id.symbol)
        val name: TextView = itemView.findViewById(R.id.name)
        val type: TextView = itemView.findViewById(R.id.price)
        val root: ConstraintLayout = itemView.findViewById(R.id.root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.item_search_stock, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.symbol.text = filterList[position].symbol
        holder.name.text = filterList[position].name
        holder.type.text = filterList[position].type

        holder.root.setOnClickListener {
            Intent(context, StockDetailActivity::class.java).apply {
                this.putExtra(Constants.stock, filterList[position])
                context.startActivity(this)
            }
        }
    }

    override fun getItemCount(): Int = filterList.size

    fun updateList(stockList: LinkedList<Stock>) {
        this.listOfStocks.addAll(stockList)
        this.filterList.addAll(stockList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var queryString = constraint.toString()
                if (queryString.isNullOrEmpty()) {
                    filterList = listOfStocks
                } else {
                    val resultList = LinkedList<Stock>()
                    for (stock in listOfStocks) {
                        if (stock.symbol.contains(queryString, true) || stock.name.contains(
                                queryString,
                                true
                            )
                        ) {
                            resultList.add(stock)
                        }
                    }
                    filterList = resultList
                }
                val filterResult = FilterResults()
                filterResult.values = filterList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as LinkedList<Stock>
                notifyDataSetChanged()
            }

        }
    }
}