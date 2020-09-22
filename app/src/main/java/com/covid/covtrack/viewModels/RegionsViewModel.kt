package com.covid.covtrack.viewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.covid.covtrack.models.Regions
import com.covid.covtrack.utilities.Constants
import com.covid.covtrack.utilities.NetworkManager
import com.covid.covtrack.utilities.PreferenceUtil

class RegionsViewModel : ViewModel(){

    val TAG = javaClass.simpleName
    var regionsReponse = MutableLiveData <List<Regions.AllRegions>>()
    var progressDialog = MutableLiveData<Boolean>()
    var alertMessage = MutableLiveData<String>()


    @SuppressLint("CheckResult")
    fun getRegions(){
        progressDialog.value = true
        NetworkManager().getRegions()
            .subscribe({
                progressDialog.value = false
                val status = it.err_code
                if (status.equals("valid")) {
                    regionsReponse.value = it.data

                } else alertMessage.value = it.message
            }, {
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.message)
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())

            })
    }

}