package com.covid.covtrack.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.covid.covtrack.models.BaseResponse
import com.covid.covtrack.models.UserDetails
import com.covid.covtrack.utilities.Constants
import com.covid.covtrack.utilities.NetworkManager
import java.lang.Exception
import java.util.*


class RegistrationViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = javaClass.simpleName
    var mcontext: Context? = null

    init {
        mcontext = application
    }

    var userName: String? = null
    var userMobile: String? = null
    var userDob: String? = null
    var userAge: String? = ""
    var userPinCode: String? = ""
    var useraddress: String? = ""
    var userQualification: String? = "Qualification"
    var isVolunteer: String? = "no"
    var isworker: String? = "no"
    var language: String? = "English";


    var registrationResp = MutableLiveData<BaseResponse>()
    var updateUserResp = MutableLiveData<BaseResponse>()
    var userProfileResp = MutableLiveData<UserDetails>()
    var progressDialog = MutableLiveData<Boolean>()
    var alertMessage = MutableLiveData<String>()


    fun validateLoginFields(): String {
        if (TextUtils.isEmpty(userName)) return Constants.validateUserName
        else if (TextUtils.isEmpty(userMobile)) return Constants.validateUserMobile
        else if (userMobile!!.length < 10) return Constants.validateUserMobileInvalid
        else if (userDob.isNullOrBlank()) return Constants.validateDob
        // else if (userQualification.equals("Qualification")) return Constants.validateQualification
        else return Constants.validationSuccess
    }

    fun getAge(_year: Int, _month: Int, _day: Int): Int {

        val cal = GregorianCalendar()
        val y: Int
        val m: Int
        val d: Int
        var a: Int

        y = cal.get(Calendar.YEAR)
        m = cal.get(Calendar.MONTH)
        d = cal.get(Calendar.DAY_OF_MONTH)
        cal.set(_year, _month, _day)
        a = y - cal.get(Calendar.YEAR)
        if (m < cal.get(Calendar.MONTH) || m == cal.get(Calendar.MONTH) && d < cal
                .get(Calendar.DAY_OF_MONTH)
        ) {
            --a
        }
        try {
            Log.d(TAG, "Age: " + a)
            if (a < 0) alertMessage.value = "Age should be greater than 1 year"
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


        return a
    }


    @SuppressLint("CheckResult")
    fun signup() {
        progressDialog.value = true
        NetworkManager().signup(
            userName!!,
            userMobile!!,
            userDob!!,
            userAge!!,
            useraddress!!,
            userQualification!!,
            isVolunteer!!,
            isworker!!,
            userPinCode!!
        )
            .subscribe({
                progressDialog.value = false
                registrationResp.value = it

            }, {
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())

            })

    }


    @SuppressLint("CheckResult")
    fun getProfile(userId: String) {
        progressDialog.value = true
        NetworkManager().getProfile(userId)
            .subscribe({
                progressDialog.value = false
                userProfileResp.value = it

            }, {
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())

            })

    }

    @SuppressLint("CheckResult")
    fun updateProfile(userId: String) {
        progressDialog.value = true
        NetworkManager().updateProfile(
            userId,
            userName!!,
            userMobile!!,
            userDob!!,
            userAge!!,
            useraddress!!,
            userQualification!!,
            isVolunteer!!,
            isworker!!,
            userPinCode!!
        )
            .subscribe({
                progressDialog.value = false
                updateUserResp.value = it

            }, {
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())

            })

    }


}