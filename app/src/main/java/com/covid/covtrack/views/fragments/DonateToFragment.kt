package com.covid.covtrack.views.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.covid.covtrack.R
import com.covid.covtrack.utilities.Constants
import kotlinx.android.synthetic.main.fragment_donate_to.*

class DonateToFragment :BaseFragment(), View.OnClickListener{
    var root: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (root == null) root =
            inflater.inflate(R.layout.fragment_donate_to, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_donate_cm_relief_fund.setOnClickListener{
            openBrowser(Constants.URL_DONATE, requireActivity())
        }
    }

    override fun onClick(v: View?) {
        Log.d("BUTTON CLICK","@@@@@@@@")
//        val id = v!!.id
//        if (id == R.id.btn_donate_cm_relief_fund) openBrowser(Constants.URL_DONATE, requireActivity())
    }

    fun openBrowser(url : String,context: Context){
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent);
    }
}