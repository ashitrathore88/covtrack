package com.covid.covtrack.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.covid.covtrack.R
import com.covid.covtrack.databinding.ActivityOtpBinding
import com.covid.covtrack.utilities.AlertMessages
import com.covid.covtrack.utilities.Constants
import com.covid.covtrack.utilities.Constants.USER_MOBILE
import com.covid.covtrack.viewModels.OtpViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import kotlinx.android.synthetic.main.activity_login.tie_username
import kotlinx.android.synthetic.main.activity_login.til_username
import kotlinx.android.synthetic.main.activity_otp.*
import java.util.concurrent.TimeUnit


class OTPActivity : BaseActivity(), View.OnClickListener {

    internal var TAG = javaClass.simpleName
    var otpViewModel: OtpViewModel? = null
    var userNumber: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        supportActionBar!!.hide()

        initlizeDataBinding()

        btn_submit.setOnClickListener(this)

        otpViewModel!!.loginResp.observe(this, Observer {
            launchOTPActivity()
        })

        otpViewModel!!.progressDialog.observe(this, Observer {
            showProgress(it)
        })

        otpViewModel!!.alertMessage.observe(this, Observer {
            AlertMessages.getInstance().alertMsgBox(it, this)
        })

        otpViewModel!!.initPhoneAuth()
        otpViewModel!!.startPhoneNumberVerification(Constants.PLUS+Constants.PHONECODE+userNumber,this@OTPActivity)
        //otpViewModel!!.startPhoneNumberVerification(Constants.PLUS+Constants.PHONECODE+"221772916",this@OTPActivity)
    }

    private fun initlizeDataBinding() {
        // registering the viewmodel
        otpViewModel = ViewModelProvider(this).get(OtpViewModel::class.java)
        val binding: ActivityOtpBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_otp)
        binding.otpViewmodel = otpViewModel //binding viewMdel to xml

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            userNumber = intent.getStringExtra(USER_MOBILE)!!
            otpViewModel!!.userMobile = userNumber

            Log.d(TAG,"Mobile num is "+userNumber)
        }
    }

    private fun launchOTPActivity() {
      //  val i1 = Intent(this@OTPActivity, HomeActivity::class.java)
        val i1 = Intent(this@OTPActivity, NavDrawerActivity::class.java)
        i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i1)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.btn_submit) {
            checkValidation()
        }
    }

    private fun checkValidation() {

        til_username.error = null

        when (otpViewModel!!.validateLoginFields()) {
            Constants.validationSuccess -> {
                // Changed to Firebase Authentication
                //otpViewModel!!.validateOTP()

                otpViewModel!!.verifyPhoneNumberWithCode(otpViewModel!!.userOTP!!)
            }
            Constants.validateOTP -> {
                til_username.error = getString(R.string.mandatoryField)
                tie_username.requestFocus()
            }
            Constants.validateOTPInvalid -> {
                til_username.error = getString(R.string.enterValidOTP)
                tie_username.requestFocus()
            }
        }
    }

    private fun showProgress(show: Boolean = false) {
        AlertMessages.getInstance().displayProgress(this, show)
    }

}
