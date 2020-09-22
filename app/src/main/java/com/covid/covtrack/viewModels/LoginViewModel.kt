package com.covid.covtrack.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.covid.covtrack.models.BaseResponse
import com.covid.covtrack.utilities.Constants
import com.covid.covtrack.utilities.NetworkManager


class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = javaClass.simpleName
    var mcontext: Context? = null

    init {
        mcontext = application
    }

    var userMobile: String? = null

    var loginResp = MutableLiveData<BaseResponse>()
    var progressDialog = MutableLiveData<Boolean>()
    var alertMessage = MutableLiveData<String>()

    fun validateLoginFields(): String {
        if (TextUtils.isEmpty(userMobile)) return Constants.validateUserMobile
        else if (userMobile!!.length < 10) return Constants.validateUserMobileInvalid
        else return Constants.validationSuccess
    }

    @SuppressLint("CheckResult")
    fun loginUser() {
        progressDialog.value = true


        NetworkManager().login(userMobile!!)
            .subscribe({
                progressDialog.value = false
                loginResp.value = it

            }, {
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())

            })


    }


}