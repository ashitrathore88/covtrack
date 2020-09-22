package com.covid.covtrack.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.covid.covtrack.R
import com.covid.covtrack.adapters.CovidNewsRv
import com.covid.covtrack.models.CovidNews
import com.covid.covtrack.utilities.AlertMessages
import com.covid.covtrack.viewModels.NewsViewModel
import com.covid.covtrack.views.activities.NewsWebViewActivity
import kotlinx.android.synthetic.main.fragment_news.*

class VideosFragment:Fragment(), CovidNewsRv.OnNewsListener{
    private lateinit var newsViewModel: NewsViewModel
    var root: View? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        if(root == null) root = inflater.inflate(R.layout.fragment_news, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel.getCovidVideos()
        newsViewModel.covidNewsResponse.observe(this, Observer {

            val status = it.status
            if (status.equals("valid")) {
                val newsList = ArrayList<CovidNews.NewsData>()
                newsList.addAll(it.data!!)
                loadNewsItems(newsList)
                tv_no_records.visibility =View.GONE
            } else {
                tv_no_records.visibility =View.VISIBLE
                if (activity != null) AlertMessages.getInstance().alertMsgBox(it.message!!, activity!!)
            }
        })

        newsViewModel.progressDialog.observe(this, Observer {
            if (activity != null){

                // showProgress(it)
                if(it){ progress_loader.visibility = View.VISIBLE}
                else progress_loader.visibility = View.INVISIBLE
            }
        })

        newsViewModel.alertMessage.observe(this, Observer {
            if (activity != null) AlertMessages.getInstance().alertMsgBox(it, activity!!)
        })
    }
    private fun loadNewsItems(newsList: java.util.ArrayList<CovidNews.NewsData>) {
        val rv_realtimestas = view!!.findViewById<RecyclerView>(R.id.rv_covid_news)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_realtimestas!!.layoutManager = linearLayoutManager
        rv_realtimestas.setHasFixedSize(true)
        val covidStatsRv = CovidNewsRv(newsList,this,true)
        rv_realtimestas.adapter = covidStatsRv
    }

    override fun onItemClick(url:String, position: Int) {
        var intent = Intent(requireContext(), NewsWebViewActivity::class.java);
        intent.putExtra("newsId",url)
        intent.putExtra("title","Videos")
        startActivity(intent)
//        val bundle = Bundle()
//        bundle.putString("newsId", url)
//        NavHostFragment.findNavController(this).navigate(R.id.action_navigation_news_to_newsViewFragment, bundle)
    }

    private fun showProgress(show: Boolean = false) {
        AlertMessages.getInstance().displayProgress(activity!!, show)
    }

    override fun onResume() {
        super.onResume()
        if (activity != null) showProgress(false)
    }

}