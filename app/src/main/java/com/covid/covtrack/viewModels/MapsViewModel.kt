package com.covid.covtrack.viewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.covid.covtrack.models.CovidOnMap
import com.covid.covtrack.utilities.NetworkManager

class MapsViewModel : ViewModel() {

    val TAG = javaClass.simpleName
    var covidMapsResponse = MutableLiveData<CovidOnMap>()
    var progressDialog = MutableLiveData<Boolean>()
    var alertMessage = MutableLiveData<String>()

    @SuppressLint("CheckResult")
    fun getCovidMaps() {
        progressDialog.value = true
        NetworkManager().getCovidMaps()
            .subscribe({
                progressDialog.value = false
                covidMapsResponse.value = it
            }, {
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())
            })

    }

}