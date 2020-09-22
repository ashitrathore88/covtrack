package com.covid.covtrack.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.covid.covtrack.R
import com.covid.covtrack.models.CovidStatistics
import java.util.ArrayList

class SubDivisionRv(val statsList: ArrayList<CovidStatistics.CovidSubDivision>) :
    RecyclerView.Adapter<SubDivisionRv.ViewHolder>() {

    private lateinit var parentContext : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        parentContext = parent.context
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_sub_division, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = statsList[position]
        holder.tv_title.setText(model.city)
        holder.tv_confirmed.text = parentContext!!.getString(R.string.str_confirmed)+"\n"+model.confirmed
        holder.tv_curved.text = parentContext!!.getString(R.string.str_cured)+"\n"+model.recovered
        holder.tv_deaths.text = parentContext!!.getString(R.string.str_deaths)+"\n"+model.deaths
    }

    override fun getItemCount(): Int {
        return statsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_title = itemView.findViewById(R.id.tv_title) as TextView
        val tv_confirmed = itemView.findViewById(R.id.tv_confirmed) as TextView
        val tv_curved = itemView.findViewById(R.id.tv_curved) as TextView
        val tv_deaths = itemView.findViewById(R.id.tv_death) as TextView
    }
}