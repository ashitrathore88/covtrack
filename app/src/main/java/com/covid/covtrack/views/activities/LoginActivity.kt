package com.covid.covtrack.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.covid.covtrack.R
import com.covid.covtrack.databinding.ActivityLoginBinding
import com.covid.covtrack.utilities.AlertMessages
import com.covid.covtrack.utilities.Constants
import com.covid.covtrack.utilities.Constants.USER_MOBILE
import com.covid.covtrack.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    internal var TAG = javaClass.simpleName
    var loginViewModel: LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        supportActionBar!!.hide()
        initlizeDataBinding()

        btn_login.setOnClickListener(this)

        loginViewModel!!.loginResp.observe(this, Observer {

            val status = it.status
            if (status.equals("valid")) {
                launchOTPActivity(true)
            } else {
                launchOTPActivity(false)
            }
        })

        loginViewModel!!.progressDialog.observe(this, Observer {
            showProgress(it)
        })

        loginViewModel!!.alertMessage.observe(this, Observer {
            AlertMessages.getInstance().alertMsgBox(it, this)
        })

    }

    private fun initlizeDataBinding() {
        // registering the viewmodel
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginViewmodel = loginViewModel //binding viewMdel to xml
    }

    private fun launchOTPActivity(flag: Boolean) {
        val i1: Intent?
        if (flag) i1 = Intent(this@LoginActivity, OTPActivity::class.java)
        else i1 = Intent(this@LoginActivity, RegistrationActivity::class.java)
        i1.putExtra("from", false)
        i1.putExtra(USER_MOBILE, tie_username.text.toString())
        startActivity(i1)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.btn_login) {
            checkValidation()
        }
    }

    private fun checkValidation() {

        til_username.error = null

        when (loginViewModel!!.validateLoginFields()) {
            Constants.validationSuccess -> {
                loginViewModel!!.loginUser()
            }
            Constants.validateUserMobile -> {
                til_username.error = getString(R.string.mandatoryField)
                tie_username.requestFocus()
            }
            Constants.validateUserMobileInvalid -> {
                til_username.error = getString(R.string.enterValidMobile)
                tie_username.requestFocus()
            }
        }
    }


    private fun showProgress(show: Boolean = false) {
        AlertMessages.getInstance().displayProgress(this, show)
    }

}
