package com.covid.covtrack.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.covid.covtrack.R
import com.covid.covtrack.models.CovidStatistics
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.ArrayList

class OtherRegionsRv (val statsList: ArrayList<CovidStatistics.CovidDivision>, val listner : onSourceClickListener ) :
    RecyclerView.Adapter<OtherRegionsRv.ViewHolder>() {
    private lateinit var parentContext : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        parentContext = parent.context
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_sub_division, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = statsList[position]
        holder.footer.visibility = View.VISIBLE
        holder.tv_last_updated.text = parentContext.getString(R.string.str_last_updated)+formatDate(model.last_updated)
        holder.tv_title.setText(model.place_name)
        holder.tv_confirmed.text = parentContext.getString(R.string.str_active)+"\n"+model.active
        holder.tv_curved.text = parentContext.getString(R.string.str_recovered)+"\n"+model.recovered
        holder.tv_deaths.text = parentContext.getString(R.string.str_deaths)+"\n"+model.deaths

        holder.tv_source.setOnClickListener {
            listner.onSourceClick(model.source)
        }
    }
    override fun getItemCount(): Int {
        return statsList.size
    }

    fun formatDate(dateStr: String): String{
        val parser =  SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMMM, yyyy")
        val formattedDate = formatter.format(parser.parse(dateStr)!!)
        return formattedDate
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_title = itemView.findViewById(R.id.tv_title) as TextView
        val tv_confirmed = itemView.findViewById(R.id.tv_confirmed) as TextView
        val tv_curved = itemView.findViewById(R.id.tv_curved) as TextView
        val tv_deaths = itemView.findViewById(R.id.tv_death) as TextView
        val tv_last_updated = itemView.findViewById<TextView>(R.id.tv_last_updated)
        val tv_source = itemView.findViewById<TextView>(R.id.tv_source)
        val footer = itemView.findViewById<RelativeLayout>(R.id.sub_division_footer)
    }

    interface onSourceClickListener{
        fun onSourceClick(sourceUrl:String)
    }
}