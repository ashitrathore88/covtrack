package com.covid.covtrack.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.covid.covtrack.R
import com.covid.covtrack.app.AppController
import com.covid.covtrack.models.CovidStatistics
import java.util.ArrayList

class CovidStatsRv(
    val statsList: ArrayList<CovidStatistics.CovidDivision>, val listener: onItemClickListener) :
    RecyclerView.Adapter<CovidStatsRv.ViewHolder>() {

    private lateinit var parentContext : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_covid_stats, parent, false)
        parentContext = parent.context
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = statsList[position]
        holder.tv_title.setText(model.place_name)
        holder.tv_tested.text = parentContext.getString(R.string.str_tested)+"\n"+model.total_tested
        holder.tv_positive.text = parentContext.getString(R.string.str_postive)+"\n"+model.positive
        holder.tv_negative.text = parentContext.getString(R.string.str_negative)+"\n"+model.negative
        holder.tv_recovered.text = parentContext.getString(R.string.str_recovered)+"\n"+model.recovered
        holder.tv_active.text = parentContext.getString(R.string.str_active)+"\n"+model.active
        holder.tv_deaths.text = parentContext.getString(R.string.str_deaths)+"\n"+model.deaths
        holder.tv_result_waited.text = parentContext.getString(R.string.str_result_awaited)+"\n"+model.result_awaited
//        holder.tv_tested.text = parentContext!!.getString(R.string.str_tested)+"\n"+model.total_tested
//        holder.tv_confirmed.text = parentContext!!.getString(R.string.str_confirmed)+"\n"+model.deaths
//        holder.tv_recorved.text = parentContext!!.getString(R.string.str_recovered)+"\n"+model.recovered
//        holder.tv_deaths.text = parentContext!!.getString(R.string.str_deaths)+"\n"+model.deaths
//        holder.tv_result_waited.text = parentContext!!.getString(R.string.str_result_awaited)+"\n"+model.active
//        holder.tv_results.text = parentContext!!.getString(R.string.str_results)+"\n"+model.postive
        //holder.tv_home_quarantined.text = parentContext!!.getString(R.string.str_home_quarantined)+"\n"+model.home_quarantine
        //holder.tv_hospital_quarantined.text = parentContext!!.getString(R.string.str_home_quarantined)+"\n"+model.hospital_quarantine

        holder.btn_sub_div.setOnClickListener(View.OnClickListener {
            listener.onItemClick(position,model.subs)
        })
    }

    override fun getItemCount(): Int {
        return 1//statsList.size
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_title = itemView.findViewById(R.id.tv_title) as TextView
        val tv_tested = itemView.findViewById(R.id.tv_tested) as TextView
        val tv_positive = itemView.findViewById<TextView>(R.id.tv_positive)
        val tv_negative = itemView.findViewById<TextView>(R.id.tv_negative)
        val tv_recovered = itemView.findViewById<TextView>(R.id.tv_recovered)
        val tv_active = itemView.findViewById<TextView>(R.id.tv_active)
        val tv_deaths = itemView.findViewById<TextView>(R.id.tv_deaths)
        val tv_result_waited = itemView.findViewById(R.id.tv_result_awaited) as TextView
//        val tv_confirmed = itemView.findViewById(R.id.tv_confirmed) as TextView
//        val tv_recorved = itemView.findViewById(R.id.tv_recorved) as TextView
//        val tv_deaths = itemView.findViewById(R.id.tv_deaths) as TextView
//
//        val tv_result_waited = itemView.findViewById(R.id.tv_result_waited) as TextView
//        val tv_results = itemView.findViewById(R.id.tv_results) as TextView
//        val tv_home_quarantined = itemView.findViewById(R.id.tv_home_quarantined) as TextView
//        val tv_hospital_quarantined = itemView.findViewById(R.id.tv_hospital_quarantined) as TextView
        val btn_sub_div = itemView.findViewById(R.id.btn_sub_div) as Button

    }

    interface onItemClickListener{
        fun onItemClick(position: Int, itemId: List<CovidStatistics.CovidSubDivision>)
    }
}