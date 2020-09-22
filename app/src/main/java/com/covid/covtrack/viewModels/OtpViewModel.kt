package com.covid.covtrack.viewModels

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.covid.covtrack.models.BaseResponse
import com.covid.covtrack.utilities.Constants
import com.covid.covtrack.utilities.NetworkManager
import com.covid.covtrack.utilities.PreferenceUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class OtpViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = javaClass.simpleName
    var mcontext: Context? = null
    var userMobile:String = ""
    var activity: Activity? = null

    init {
        mcontext = application
    }

    var userOTP: String? = null
    private var mAuth: FirebaseAuth? = null
    private var verificationCode: String? = null

    var loginResp = MutableLiveData<BaseResponse>()
    var progressDialog = MutableLiveData<Boolean>()
    var alertMessage = MutableLiveData<String>()

    fun validateLoginFields(): String {
        if (TextUtils.isEmpty(userOTP)) return Constants.validateOTP
        else if (userOTP!!.length < 6) return Constants.validateOTPInvalid
        else return Constants.validationSuccess
    }

    @SuppressLint("CheckResult")
    fun validateOTP() {
        progressDialog.value = true
       // NetworkManager().verifyOtp(userMobile,userOTP!!)
        NetworkManager().verifyOtp(userMobile,verificationCode!!)
            .subscribe({
                progressDialog.value = false
                val status = it.status
                if (status.equals("valid")) {
                    PreferenceUtil.ins.saveValue(Constants.USER_ID, it.data!!.id!!)
                    PreferenceUtil.ins.saveValue(Constants.USER_NAME, it.data!!.name!!)
                    PreferenceUtil.ins.saveValue(Constants.USER_MOBILE, it.data!!.mobile!!)
                    loginResp.value = it
                } else alertMessage.value = it.message

            }, {
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())

            })

    }
    @SuppressLint("CheckResult")
    fun saveOTP(){
        progressDialog.value = true
        NetworkManager().saveOtp(userMobile,verificationCode!!)
            .subscribe({
                progressDialog.value = false
//                val status = it.status
//                if (status.equals("valid")) {
//                    PreferenceUtil.ins.saveValue(Constants.USER_ID, it.data!!.id!!)
//                    PreferenceUtil.ins.saveValue(Constants.USER_NAME, it.data!!.name!!)
//                    PreferenceUtil.ins.saveValue(Constants.USER_MOBILE, it.data!!.mobile!!)
//                    //loginResp.value = it
//                } else alertMessage.value = it.message

            }, {
                progressDialog.value = false
                alertMessage.value = it.message
                Log.d(TAG, "sri_ErrorResp: " + it.printStackTrace())

            })

    }

    fun initPhoneAuth(){
        mAuth = FirebaseAuth.getInstance()
    }



    fun startPhoneNumberVerification(mobileNum: String,context: Activity){
        Log.d(TAG, "startPhoneNumberVerification :: "+mobileNum)
        activity = context
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mobileNum,
            120,
            TimeUnit.SECONDS,
            context,
            mCallBack
        )
    }

    fun verifyPhoneNumberWithCode( otp: String){

        var credential = PhoneAuthProvider.getCredential(verificationCode!!,otp)
        signInWithPhoneAuthCredetial(credential)
    }

    private fun signInWithPhoneAuthCredetial(credential: PhoneAuthCredential){
        progressDialog.value = true
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    progressDialog.value = false
                  //  val user = task.result?.user

                    validateOTP()


                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    progressDialog.value = false
                    alertMessage.value = task.exception!!.message
                    Log.d(TAG, "sri_ErrorResp: " + task.exception!!.message)

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }

    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                Log.d(TAG, "onCodeSent :: "+s)
                verificationCode = s;
                saveOTP()
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    Log.d(TAG, "onVerificationCompleted :: ")
                    signInWithPhoneAuthCredetial(phoneAuthCredential)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d(TAG, "onVerificationFailed :: "+e.message)
            }
        }
}