package com.covid.covtrack.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.covid.covtrack.R
import com.covid.covtrack.adapters.CovidHospitalsRv
import com.covid.covtrack.adapters.RegionsRv
import com.covid.covtrack.models.Hospitals
import com.covid.covtrack.models.Regions
import com.covid.covtrack.utilities.AlertMessages
import com.covid.covtrack.utilities.PreferenceUtil
import com.covid.covtrack.viewModels.CovidHospitalViewModel
import com.covid.covtrack.viewModels.RegionsViewModel

class CovidHospitalsFragment :BaseFragment(){

    private lateinit var hospitalsViewModel: CovidHospitalViewModel
    var root: View? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hospitalsViewModel = ViewModelProvider(this).get(CovidHospitalViewModel::class.java)
        if (root == null) root =
            inflater.inflate(R.layout.fragment_hospitals_list, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hospitalsViewModel.getHospitals()

        hospitalsViewModel.hospitalsReponse.observe(this, Observer {
            var hospitalsList = ArrayList<Hospitals.Hospital>()
            hospitalsList.addAll(it)
            loadHospitalsData(hospitalsList)

        })

        hospitalsViewModel.progressDialog.observe(this, Observer {
            if (activity != null) showProgress(it)
        })

        hospitalsViewModel.alertMessage.observe(this, Observer {
            if (activity != null) AlertMessages.getInstance().alertMsgBox(it, requireActivity())
        })
    }

    private fun loadHospitalsData(hospitalList: ArrayList<Hospitals.Hospital>){
        val rv_hospitals_list = view!!.findViewById<RecyclerView>(R.id.rv_hospitals_list)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_hospitals_list!!.layoutManager = linearLayoutManager
        rv_hospitals_list.setHasFixedSize(true)

        val covidHospitalsRv = CovidHospitalsRv(hospitalList)
        rv_hospitals_list.adapter = covidHospitalsRv
    }

    private fun showProgress(show: Boolean = false) {
        AlertMessages.getInstance().displayProgress(requireActivity(), show)
    }
}