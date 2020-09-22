package com.covid.covtrack.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.covid.covtrack.R
import com.covid.covtrack.adapters.RegionsRv
import com.covid.covtrack.models.Regions
import com.covid.covtrack.utilities.AlertMessages
import com.covid.covtrack.viewModels.CovidStatsViewModel
import com.covid.covtrack.viewModels.RegionsViewModel

class ConnectToDoctorFragment : BaseFragment(), RegionsRv.onItemClickListener {


    private lateinit var regionsViewModel: RegionsViewModel
    var root: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        regionsViewModel = ViewModelProvider(this).get(RegionsViewModel::class.java)
        if (root == null) root =
            inflater.inflate(R.layout.fragment_connect_to_doctor, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        regionsViewModel.getRegions()
        regionsViewModel.regionsReponse.observe(this, Observer {
            var regionsList = ArrayList<Regions.AllRegions>()
            regionsList.addAll(it)
            loadDoctorsList(regionsList)
        })

        regionsViewModel.progressDialog.observe(this, Observer {
            if (activity != null) showProgress(it)
        })
        regionsViewModel.alertMessage.observe(this, Observer {
            if (activity != null) AlertMessages.getInstance().alertMsgBox(it, requireActivity())
        })
    }

    private fun loadDoctorsList(regionsList: ArrayList<Regions.AllRegions>) {
        val rv_regions_list = view!!.findViewById<RecyclerView>(R.id.rv_regions_list)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_regions_list!!.layoutManager = linearLayoutManager
        rv_regions_list.setHasFixedSize(true)
        //rv_regions_list.addItemDecoration(DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL))
        val regionsRv = RegionsRv(regionsList, this)
        rv_regions_list.adapter = regionsRv
    }

    override fun onItemClick(position: Int, itemId: Regions.AllRegions) {
        val bundle = Bundle()
        bundle.putString("regionId", itemId.id)
        bundle.putString("regionName", itemId.name)
        var fragment = DoctorsListFragment::class.java.newInstance()
        fragment.arguments = bundle
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment!!).addToBackStack(null)
            .commit();
    }

    private fun showProgress(show: Boolean = false) {
        AlertMessages.getInstance().displayProgress(requireActivity(), show)
    }



}