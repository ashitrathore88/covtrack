package com.covid.covtrack.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.covid.covtrack.models.Doctors
import com.covid.covtrack.utilities.NetworkManager

class DoctorsViewModel :ViewModel(){
    val TAG = javaClass.simpleName
    val fetchDocResponse = MutableLiveData <List<Doctors.Doctor>>()
    var progressDialog = MutableLiveData<Boolean>()
    var alertMessage = MutableLiveData<String>()
    var regionId: String? = null
    @SuppressLint("CheckResult")
    fun getDoctors(){
        progressDialog.value = true
        NetworkManager().getDoctors(regionId!!)
            .subscribe({
                progressDialog.value = false
                val status = it.err_code
                if (status.equals("valid")) {
                    fetchDocResponse.value = it.data

                } else alertMessage.value = it.message
            },{
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.message)
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())
            })
    }

    fun dialNumber(number: String,context: Context){
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(number)))
        context.startActivity(intent)
    }
}