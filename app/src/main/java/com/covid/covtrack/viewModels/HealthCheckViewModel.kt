package com.covid.covtrack.viewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.covid.covtrack.models.BaseResponse
import com.covid.covtrack.models.CovidNews
import com.covid.covtrack.models.CovidQuestions
import com.covid.covtrack.utilities.Constants
import com.covid.covtrack.utilities.Constants.ErrorMsg
import com.covid.covtrack.utilities.NetworkManager
import com.covid.covtrack.utilities.PreferenceUtil
import com.google.gson.JsonObject
import org.json.JSONObject

class HealthCheckViewModel : ViewModel() {

    val TAG = javaClass.simpleName
    var covidquestionsResponse = MutableLiveData<CovidQuestions>()
    var covidAnsersResponse = MutableLiveData<BaseResponse>()
    var progressDialog = MutableLiveData<Boolean>()
    var alertMessage = MutableLiveData<String>()


    @SuppressLint("CheckResult")
    fun getCovidQuestions() {
        val user_ID = PreferenceUtil.ins.getValueString(Constants.USER_ID)
        progressDialog.value = true
        NetworkManager().getCovidQuestions(user_ID!!)
            .subscribe({
                progressDialog.value = false
                covidquestionsResponse.value = it
            }, {
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())
            })

    }

    @SuppressLint("CheckResult")
    fun uploadAnswers(jsonObject: JsonObject) {
        progressDialog.value = true
        NetworkManager().uploadAnswers(jsonObject)
            .subscribe({
                progressDialog.value = false
                covidAnsersResponse.value = it
            }, {
                progressDialog.value = false
                alertMessage.value = ErrorMsg
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())
            })

    }

}