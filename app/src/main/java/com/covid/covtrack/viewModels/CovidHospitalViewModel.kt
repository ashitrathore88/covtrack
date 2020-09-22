package com.covid.covtrack.viewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.covid.covtrack.models.Hospitals
import com.covid.covtrack.models.Regions
import com.covid.covtrack.utilities.NetworkManager

class CovidHospitalViewModel : ViewModel(){
    val TAG = javaClass.simpleName

    var hospitalsReponse = MutableLiveData <List<Hospitals.Hospital>>()
    var progressDialog = MutableLiveData<Boolean>()
    var alertMessage = MutableLiveData<String>()


    @SuppressLint("CheckResult")
    fun getHospitals(){
        progressDialog.value = true
        NetworkManager().getHospitals()
            .subscribe({
                progressDialog.value = false
                val status = it.err_code
                if (status.equals("valid")) {
                    hospitalsReponse.value = it.data

                } else alertMessage.value = it.message
            },{
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.message)
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())
            })
    }
}