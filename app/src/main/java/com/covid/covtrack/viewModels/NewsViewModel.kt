package com.covid.covtrack.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.covid.covtrack.models.CovidNews
import com.covid.covtrack.utilities.NetworkManager

class NewsViewModel : ViewModel() {

    val TAG = javaClass.simpleName
    var covidNewsResponse = MutableLiveData<CovidNews>()
    var progressDialog = MutableLiveData<Boolean>()
    var alertMessage = MutableLiveData<String>()

    @SuppressLint("CheckResult")
    fun getCovidNews() {
        progressDialog.value = true
        NetworkManager().getCovidNews()
            .subscribe({

                progressDialog.value = false
                covidNewsResponse.value = it

            }, {
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())
            })

    }

    @SuppressLint("CheckResult")
    fun getCovidGovNews() {
        progressDialog.value = true
        NetworkManager().getCovidGovNews()
            .subscribe({
                progressDialog.value = false
                covidNewsResponse.value = it

            }, {
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())
            })

    }

    @SuppressLint("CheckResult")
    fun getCovidVideos() {
        progressDialog.value = true
        NetworkManager().getCovidVideo()
            .subscribe({
                progressDialog.value = false
                covidNewsResponse.value = it

            }, {
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())
            })

    }

    fun openBrowser(url : String,context: Context){
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent);
    }

}