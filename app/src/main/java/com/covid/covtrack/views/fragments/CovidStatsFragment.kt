package com.covid.covtrack.views.fragments

import android.content.Intent
import android.os.Bundle
import android.transition.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.covid.covtrack.R
import com.covid.covtrack.adapters.CovidStatsRv
import com.covid.covtrack.adapters.OtherRegionsRv
import com.covid.covtrack.adapters.SubDivisionRv
import com.covid.covtrack.models.CovidStatistics
import com.covid.covtrack.utilities.AlertMessages
import com.covid.covtrack.utilities.Constants.SUB_DIVISION_LIST
import com.covid.covtrack.viewModels.CovidStatsViewModel
import com.covid.covtrack.views.activities.HomeActivity
import com.covid.covtrack.views.activities.NavDrawerActivity
import com.covid.covtrack.views.activities.ReportGatheringActivity
import kotlinx.android.synthetic.main.fragment_covid_stats.*


class CovidStatsFragment : BaseFragment(), View.OnClickListener, CovidStatsRv.onItemClickListener, OtherRegionsRv.onSourceClickListener {


    private lateinit var covidStatsViewModel: CovidStatsViewModel
    var root: View? = null
    //private lateinit var layout_container:RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        covidStatsViewModel = ViewModelProvider(this).get(CovidStatsViewModel::class.java)
        if (root == null) root = inflater.inflate(R.layout.fragment_covid_stats, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // header_container = view.findViewById(R.id.header_container)

        layout_container.visibility = View.INVISIBLE
        covidStatsViewModel.getCovidStatus()
        covidStatsViewModel.covidStatsResponse.observe(this, Observer {
            val statsList = ArrayList<CovidStatistics.CovidDivision>()
            statsList.addAll(it)
            loadCovidStats(statsList)
            val statsSubList = ArrayList<CovidStatistics.CovidSubDivision>()
            statsSubList.addAll(statsList.get(0).subs)
            loadCovidSubStats(statsSubList)
            layout_container.visibility = View.VISIBLE

            loadOtherRegionsStats(statsList)

        })

        covidStatsViewModel.progressDialog.observe(this, Observer {
            if (activity != null) showProgress(it)
        })

        covidStatsViewModel.alertMessage.observe(this, Observer {
            if (activity != null) AlertMessages.getInstance().alertMsgBox(it, activity!!)
        })

//        btn_healthcheck.setOnClickListener(this)
//        btn_news.setOnClickListener(this)
//        btn_map.setOnClickListener(this)
//        btn_emergency.setOnClickListener(this)
//        btn_helpline.setOnClickListener(this)
//        btn_doctor.setOnClickListener(this)
//        btn_gen_pass.setOnClickListener(this)
//        btn_report_gathering.setOnClickListener(this)
    }

    private fun loadCovidStats(statsList: ArrayList<CovidStatistics.CovidDivision>) {
        val rv_realtimestas = view!!.findViewById<RecyclerView>(R.id.rv_realtimestas)
        val linearLayoutManagerHeader = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_realtimestas!!.layoutManager = linearLayoutManagerHeader
        rv_realtimestas.setHasFixedSize(true)
        val covidStatsRv = CovidStatsRv(statsList, this)
        rv_realtimestas.adapter = covidStatsRv


    }

    private fun loadCovidSubStats(statsList: ArrayList<CovidStatistics.CovidSubDivision>) {
        val rv_subdivision = view!!.findViewById<RecyclerView>(R.id.rv_subdivision)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_subdivision!!.layoutManager = linearLayoutManager
        rv_subdivision.setHasFixedSize(true)
        val subDivisionRv = SubDivisionRv(statsList)
        rv_subdivision.adapter = subDivisionRv
    }

    private fun loadOtherRegionsStats(statsList: ArrayList<CovidStatistics.CovidDivision>){
        val rv_other_regions = view!!.findViewById<RecyclerView>(R.id.rv_other_regions)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_other_regions!!.layoutManager = linearLayoutManager
        rv_other_regions.setHasFixedSize(true)
        var otherRegionsList = statsList.drop(1)
        val otherRegionsRv = OtherRegionsRv(ArrayList(otherRegionsList), this)
        rv_other_regions.adapter = otherRegionsRv
    }


    override fun onItemClick(position: Int, itemId: List<CovidStatistics.CovidSubDivision>) {

        val statsList = ArrayList<CovidStatistics.CovidSubDivision>()
        statsList.addAll(itemId)
        val bundle = Bundle()
        bundle.putParcelableArrayList(SUB_DIVISION_LIST, statsList)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_navigation_covidstats_to_subDivisionFragment, bundle)
    }


    override fun onClick(v: View?) {
        val id = v!!.id
        if (id == R.id.btn_emergency) covidStatsViewModel.dialNumber("01672232304", activity!!)
        else if (id == R.id.btn_helpline) covidStatsViewModel.dialNumber("104", activity!!)
        else if (id == R.id.btn_doctor) covidStatsViewModel.dialNumber("9878455045", activity!!)
        else if (id == R.id.btn_gen_pass) covidStatsViewModel.openBrowser("https://epasscovid19.pais.net.in/", activity!!)
        else if (id == R.id.btn_report_gathering) launchActivity()
        else (activity as HomeActivity).clickBottomItem(id)
    }

    private fun showProgress(show: Boolean = false) {
        AlertMessages.getInstance().displayProgress(activity!!, show)
    }

    private fun launchActivity(){

        val i1 = Intent(activity!!,ReportGatheringActivity::class.java )
        startActivity(i1)
        activity!!.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onSourceClick(sourceUrl: String) {
        covidStatsViewModel.openBrowser(sourceUrl,activity!!)
    }

}

