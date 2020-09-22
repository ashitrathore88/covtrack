package com.covid.covtrack.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.covid.covtrack.R
import com.covid.covtrack.adapters.DoctorsListRv
import com.covid.covtrack.models.Doctors
import com.covid.covtrack.utilities.AlertMessages
import com.covid.covtrack.viewModels.DoctorsViewModel
import com.covid.covtrack.viewModels.RegionsViewModel
import kotlinx.android.synthetic.main.fragment_doctors_list.*


class DoctorsListFragment : BaseFragment(), DoctorsListRv.onItemClickListener{


    val TAG = javaClass.simpleName
    private lateinit var docListViewModel: DoctorsViewModel
    var root: View? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        docListViewModel = ViewModelProvider(this).get(DoctorsViewModel::class.java)

        docListViewModel.regionId = arguments?.getString("regionId")




        if (root == null) root = inflater.inflate(R.layout.fragment_doctors_list, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_title.text = arguments?.getString("regionName")
        docListViewModel.getDoctors()
        docListViewModel.fetchDocResponse.observe(this, Observer {
            var docsList = ArrayList<Doctors.Doctor>()
            docsList.addAll(it)

            loadDocList(docsList)
        })

        docListViewModel.progressDialog.observe(this, Observer {
            if (activity != null) showProgress(it)
        })

        docListViewModel.alertMessage.observe(this, Observer {
            if (activity != null) AlertMessages.getInstance().alertMsgBox(it, requireActivity())
        })
    }

    private fun loadDocList(docList : ArrayList<Doctors.Doctor>){
        val rv_doctors_list = view!!.  findViewById<RecyclerView>(R.id.rv_doctors_list)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_doctors_list!!.layoutManager = linearLayoutManager
        rv_doctors_list.setHasFixedSize(true)
        val doctorsRv = DoctorsListRv(docList,this)
        rv_doctors_list.adapter = doctorsRv
    }

    override fun onItemClick(position: Int, mobileNo: String) {
       docListViewModel.dialNumber(mobileNo,requireActivity())
    }

    private fun showProgress(show: Boolean = false) {
        AlertMessages.getInstance().displayProgress(requireActivity(), show)
    }
}