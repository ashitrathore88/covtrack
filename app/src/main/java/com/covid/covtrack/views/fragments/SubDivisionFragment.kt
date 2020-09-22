package com.covid.covtrack.views.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.covid.covtrack.R
import com.covid.covtrack.adapters.SubDivisionRv
import com.covid.covtrack.models.CovidStatistics
import com.covid.covtrack.utilities.Constants.SUB_DIVISION_LIST

class SubDivisionFragment : Fragment() {

    var subDivisionList:ArrayList<CovidStatistics.CovidSubDivision>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments != null) {
            subDivisionList = arguments!!.getParcelableArrayList<CovidStatistics.CovidSubDivision>(SUB_DIVISION_LIST)
        }
        return inflater.inflate(R.layout.fragment_sub_division, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadCovidStats(subDivisionList!!)


    }

    private fun loadCovidStats(statsList: ArrayList<CovidStatistics.CovidSubDivision>) {
        val rv_subdivision = view!!.findViewById<RecyclerView>(R.id.rv_subdivision)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_subdivision!!.layoutManager = linearLayoutManager
        rv_subdivision.setHasFixedSize(true)
        val subDivisionRv = SubDivisionRv(statsList)
        rv_subdivision.adapter = subDivisionRv
    }

}
