package com.covid.covtrack.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

import com.covid.covtrack.R
import com.covid.covtrack.adapters.NewsFragmentTabAdaptor
import com.covid.covtrack.models.CovidStatistics
import com.covid.covtrack.utilities.AlertMessages
import com.covid.covtrack.utilities.Constants
import com.covid.covtrack.utilities.PreferenceUtil
import com.covid.covtrack.viewModels.CovidStatsViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_news_tabs.*


class NewsTabsFragment : Fragment(){
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager2? = null
    private val tab_position = 0
    var root: View? =null

    var chartWebview: WebView? = null;
    private lateinit var covidStatsViewModel: CovidStatsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        covidStatsViewModel = ViewModelProvider(this).get(CovidStatsViewModel::class.java)
        if(root == null) root = inflater.inflate(R.layout.fragment_news_tabs, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById<TabLayout>(R.id.tablayout)
        viewPager = view.findViewById<ViewPager2>(R.id.viewpager)

        chartWebview = view.findViewById(R.id.graph_webview);


        covidStatsViewModel.getCovidStatus()
        covidStatsViewModel.covidStatsResponse.observe(this, Observer {
            val statsList = ArrayList<CovidStatistics.CovidDivision>()
            statsList.addAll(it)
            _setChartView(statsList);
        })

        covidStatsViewModel.progressDialog.observe(this, Observer {
            if (activity != null){

                showProgress(it)
            }


        })

        covidStatsViewModel.alertMessage.observe(this, Observer {
            if (activity != null) AlertMessages.getInstance().alertMsgBox(it, requireActivity())
        })


        tabLayout!!.addTab(tabLayout!!.newTab())
        tabLayout!!.addTab(tabLayout!!.newTab())
        tabLayout!!.addTab(tabLayout!!.newTab())
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL


        val adapter = NewsFragmentTabAdaptor(this, tabLayout!!.tabCount)
        adapter.addFragment(NewsFragment())
        adapter.addFragment(VideosFragment())
        adapter.addFragment(GovtOrdersFragment())
        viewPager!!.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager!!.adapter = adapter

        TabLayoutMediator(tablayout, viewpager) { tab, position ->
            when (position) {
                0 -> { tab.text = getString(R.string.title_news)}
                1 -> { tab.text = getString(R.string.title_videos)}
                2 -> { tab.text = getString(R.string.title_govt_orders)}
            }

        }.attach()
        var img_graph = view.findViewById<ImageView>(R.id.img_graph)
        var imgUrl = PreferenceUtil.ins.getValueString(Constants.GRAPH_IMG)

        Picasso.with(context).load(imgUrl).into(img_graph);


    }

    fun _setChartView(statsList: ArrayList<CovidStatistics.CovidDivision>){
        val total = Integer.parseInt(statsList.get(0).active)+Integer.parseInt(statsList.get(0).recovered)+Integer.parseInt(statsList.get(0).total_tested)

        val confirmedPercent = (Integer.parseInt(statsList.get(0).active).toDouble() / total) * 100
        val recoveredPercent = (Integer.parseInt(statsList.get(0).recovered).toDouble() / total) * 100
        val testedPercent = (Integer.parseInt(statsList.get(0).total_tested).toDouble() / total) * 100



        var chartUrl  =  "https://chart.googleapis.com/chart?cht=p&chs=350x175&chd=t:"+confirmedPercent+","+recoveredPercent+","+testedPercent+"&chco=f1b548|36ab92|dc4d57&chl="+getString(R.string.str_confirmed)+" ("+statsList.get(0).active+")| "+getString(R.string.str_recovered)+" ("+statsList.get(0).recovered+")| "+getString(R.string.str_tested)+" ("+statsList.get(0).total_tested+")&chtt="+getString(R.string.str_chart_title);
        Log.d("Chart URL",chartUrl)
        chartWebview!!.loadUrl(chartUrl);

    }
    private fun showProgress(show: Boolean = false) {
        AlertMessages.getInstance().displayProgress(requireActivity(), show)
    }

}