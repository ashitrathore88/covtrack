package com.covid.covtrack.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.covid.covtrack.R
import com.covid.covtrack.models.CovidQuestions
import com.covid.covtrack.utilities.Constants
import com.covid.covtrack.utilities.PreferenceUtil
import java.util.ArrayList

class CovidQuestionsRv(
    val newsList: ArrayList<CovidQuestions.Question>
) :
    RecyclerView.Adapter<CovidQuestionsRv.ViewHolder>() {
    var localeStr : String? = null
    var selectedlist: ArrayList<Boolean>? = null

    init {
        localeStr = PreferenceUtil.ins.getValueString("language");
        selectedlist = ArrayList()
        for (i in newsList.indices) {
            this.selectedlist!!.add(false)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_questions, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = newsList[position]
        try {
            if (localeStr == "pa") {
                holder.tv_question.setText(model.question.split("#")[1])
            } else if (localeStr=="en" ) {
                holder.tv_question.setText(model.question.split("#")[0])
            }else {
                holder.tv_question.setText(model.question)
            }
        }catch (e : Exception){
            holder.tv_question.setText(model.question)
        }

        holder.rb_yes.setOnClickListener {
            this.selectedlist!!.set(position, true)
        }

        holder.rb_no.setOnClickListener {
           this. selectedlist!!.set(position, false)
        }

    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_question = itemView.findViewById(R.id.tv_question) as TextView
        val rb_yes = itemView.findViewById(R.id.rb_yes) as RadioButton
        val rb_no = itemView.findViewById(R.id.rb_no) as RadioButton

    }

    fun selectedAnswers(): ArrayList<Boolean> {
        return selectedlist!!
    }

    fun checkIfTrue(): Boolean{
        for(b in selectedlist!!) if(b) return true;
        return false;
    }

}