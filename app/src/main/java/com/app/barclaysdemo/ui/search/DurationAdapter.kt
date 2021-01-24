package com.app.barclaysdemo.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.barclaysdemo.R

class DurationAdapter(
    private val context: Context,
    private val listOfDuration: MutableList<String>
) : RecyclerView.Adapter<DurationAdapter.Holder>() {
    lateinit var listener: OnDurationClick
    var lastSelected: String = ""
    public fun setDurationClickListener(onDurationClick: OnDurationClick) {
        this.listener = onDurationClick
    }

    interface OnDurationClick {
        fun onDurationClick(interval: String)
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val duration: TextView = itemView.findViewById(R.id.duration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.item_duration, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.duration.text = listOfDuration[position]
        if (lastSelected.isEmpty()) {
            holder.duration.setBackgroundResource(R.drawable.unselected_duration_bg)
        } else {
            if (lastSelected.equals(listOfDuration[position], true)) {
                holder.duration.setBackgroundResource(R.drawable.duration_bg)
            } else {
                holder.duration.setBackgroundResource(R.drawable.unselected_duration_bg)
            }
        }
        holder.duration.setOnClickListener {
            if(lastSelected.equals(listOfDuration[position],true)){
                lastSelected=""
                listener.onDurationClick("")
                notifyDataSetChanged()

            }else {
                lastSelected = listOfDuration[position]
                listener.onDurationClick(listOfDuration[position])
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = listOfDuration.size
}