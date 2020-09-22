package com.covid.covtrack.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.covid.covtrack.models.BaseResponse
import com.covid.covtrack.models.CovidStatistics
import com.covid.covtrack.utilities.Constants.GRAPH_IMG
import com.covid.covtrack.utilities.NetworkManager
import com.covid.covtrack.utilities.PreferenceUtil


class CovidStatsViewModel : ViewModel() {

    val TAG = javaClass.simpleName
    var covidStatsResponse = MutableLiveData<List<CovidStatistics.CovidDivision>>()
    var appUpdateRes = MutableLiveData<BaseResponse>()
    var progressDialog = MutableLiveData<Boolean>()
    var alertMessage = MutableLiveData<String>()

    @SuppressLint("CheckResult")
    fun getCovidStatus() {
        progressDialog.value = true
        NetworkManager().getCovidStatus()
            .subscribe({
                progressDialog.value = false
                val status = it.err_code
                if (status.equals("valid")) {
                    covidStatsResponse.value = it.data
                    PreferenceUtil.ins.saveValue(GRAPH_IMG, it.graph_image)
                } else alertMessage.value = it.message

            }, {
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.message)
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())

            })

    }

    @SuppressLint("CheckResult")
    fun checkAppUpdate(currentVersion: Int) {
        NetworkManager().getAppUpdate(currentVersion)
            .subscribe({
                val status = it.status
                if (status.equals("valid")) appUpdateRes.value = it
            }, {
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.message)
            })
    }

    fun dialNumber(number: String,context: Context){
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(number)))
        context.startActivity(intent)
    }

    fun openBrowser(url : String,context: Context){
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent);
    }


}