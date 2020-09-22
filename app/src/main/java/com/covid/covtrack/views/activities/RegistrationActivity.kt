package com.covid.covtrack.views.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.covid.covtrack.R
import com.covid.covtrack.databinding.ActivityRegistrationBinding
import com.covid.covtrack.models.BaseResponse
import com.covid.covtrack.utilities.AlertMessages
import com.covid.covtrack.utilities.Constants
import com.covid.covtrack.utilities.Constants.SRT_ENG
import com.covid.covtrack.utilities.Constants.SRT_NO
import com.covid.covtrack.utilities.Constants.SRT_PA
import com.covid.covtrack.utilities.Constants.SRT_YES
import com.covid.covtrack.utilities.Constants.USER_ID
import com.covid.covtrack.utilities.Constants.USER_MOBILE
import com.covid.covtrack.utilities.PreferenceUtil
import com.covid.covtrack.viewModels.RegistrationViewModel
import com.covid.covtrack.views.fragments.BaseFragment
import kotlinx.android.synthetic.main.activity_login.tie_username
import kotlinx.android.synthetic.main.activity_login.til_username
import kotlinx.android.synthetic.main.activity_registration.*

import java.util.*


class RegistrationActivity : BaseActivity(), View.OnClickListener,
    RadioGroup.OnCheckedChangeListener {


    private val TAG = javaClass.simpleName
    var registrationViewModel: RegistrationViewModel? = null
    var userNumber: String = ""
    var userId: String? = null
    var from: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        supportActionBar!!.hide()
        initlizeDataBinding()

        btn_submit.setOnClickListener(this)
        btn_clear.setOnClickListener(this)
        tie_userdob.setOnClickListener(this)

        registrationViewModel!!.registrationResp.observe(this, Observer {
            val status = it.status
            if (status.equals("valid")) {
                launchOTPActivity(true, it)
            } else {
                launchOTPActivity(false, it)
            }
        })

        registrationViewModel!!.updateUserResp.observe(this, Observer {
            val status = it.status
            PreferenceUtil.ins.saveValue("language",registrationViewModel!!.language!!.toLowerCase())
            BaseActivity.dLocale = Locale(registrationViewModel!!.language!!)
            BaseFragment.dLocale = Locale(registrationViewModel!!.language!!)

            setLocale(registrationViewModel!!.language!!)
            if (status.equals("valid")) {


                reloadApp()
                //onBackPressed()
            } else {
                launchOTPActivity(false, it)
            }
        })

        registrationViewModel!!.alertMessage.observe(this, Observer {
            AlertMessages.getInstance().alertMsgBox(it, this)

        })

        registrationViewModel!!.progressDialog.observe(this, Observer {
            showProgress(it)
        })

        registrationViewModel!!.userProfileResp.observe(this, Observer {

            tie_username.setText(it.data!!.name)
            tie_usermobile.setText(it.data!!.mobile)
            tie_userdob.setText(it.data!!.dob)
            tie_userage.setText(it.data!!.age)
            tie_useraddress.setText(it.data!!.address)
            tie_userpincode.setText(it.data!!.pincode)

            selectValue(spn_qualification, it.data!!.qualification!!)

            if (it.data!!.is_volunteer.equals(getString(R.string.str_yes))) rb_volunteer_yes.setChecked(true)
            else rb_volunteer_no.setChecked(true)

            if (it.data!!.is_labour.equals(getString(R.string.str_yes))) rb_worker_yes.setChecked(true)
            else rb_worker_no.setChecked(true)
            val language =  PreferenceUtil.ins.getValueString("language");
            if (language == "pa") {
                rb_lang_punjabi.isChecked = true
                rb_lang_english.isChecked = false
            } else if (language=="en" ) {
                rb_lang_punjabi.isChecked = false
                rb_lang_english.isChecked = true
            }else {
                rb_lang_punjabi.isChecked = false
                rb_lang_english.isChecked = true
            }

        })
    }

    private fun initlizeDataBinding() {
        // registering the viewmodel
        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        val binding: ActivityRegistrationBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_registration)
        binding.signupViewModel = registrationViewModel //binding viewMdel to xml

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            from = intent.getBooleanExtra("from", false)
            if (from) {
                tv_title.setText(R.string.str_profile)
                btn_clear.visibility = View.GONE
                userId = PreferenceUtil.ins.getValueString(USER_ID)
                registrationViewModel!!.getProfile(userId!!)
                tie_usermobile.setFocusable(false);
            } else {
                //Hide fields for first time
                spn_qualification_field.visibility = View.GONE
                til_userpincode.visibility = View.GONE
                til_useraddress.visibility = View.GONE
                rg_volunteer_title.visibility = View.GONE
                rg_volunteer.visibility = View.GONE
                til_userage.visibility = View.GONE
                userNumber = intent.getStringExtra(USER_MOBILE)!!
                if (userNumber.isNotEmpty()) tie_usermobile.setText(userNumber)
                registrationViewModel!!.userMobile = userNumber
            }
        }

        rg_volunteer.setOnCheckedChangeListener(this)
        rg_language.setOnCheckedChangeListener(this)
        rg_worker.setOnCheckedChangeListener(this)

        spn_qualification.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                registrationViewModel!!.userQualification = getString(R.string.str_qualification)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                registrationViewModel!!.userQualification = parent!!.selectedItem.toString()
            }

        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val id = group!!.checkedRadioButtonId
        if (id == R.id.rb_volunteer_yes) registrationViewModel!!.isVolunteer = SRT_YES
        else if (id == R.id.rb_volunteer_no) registrationViewModel!!.isVolunteer = SRT_NO
        else if (id == R.id.rb_worker_yes) registrationViewModel!!.isworker = SRT_YES
        else if (id == R.id.rb_worker_no) registrationViewModel!!.isworker = SRT_NO
        else if (id == R.id.rb_lang_english) registrationViewModel!!.language = SRT_ENG
        else if (id == R.id.rb_lang_punjabi) registrationViewModel!!.language = SRT_PA
    }

    private fun launchOTPActivity(flag: Boolean, data: BaseResponse) {
        val i1: Intent?
        if (flag) {
            i1 = Intent(this@RegistrationActivity, OTPActivity::class.java)
            i1.putExtra(USER_MOBILE, userNumber)
            startActivity(i1)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        } else {
            AlertMessages.getInstance().alertMsgBox(data.message!!, this)
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.btn_submit) {
            checkValidation()
        } else if (id == R.id.tie_userdob) {
            pickDob()
        } else if (id == R.id.btn_clear) {
            clearForm(scrollview)
            spn_qualification.setSelection(0)
            rb_volunteer_no.setChecked(true)
            rb_worker_no.setChecked(true)

        }
    }

    private fun checkValidation() {

        til_username.error = null
        til_usermoile.error = null
        til_userdob.error = null


        when (registrationViewModel!!.validateLoginFields()) {
            Constants.validationSuccess -> {
                if (from) registrationViewModel!!.updateProfile(userId!!)
                else registrationViewModel!!.signup()
            }
            Constants.validateUserName -> {
                til_username.error = getString(R.string.mandatoryField)
                tie_username.requestFocus()
            }
            Constants.validateUserMobile -> {
                til_usermoile.error = getString(R.string.mandatoryField)
                tie_usermobile.requestFocus()
            }
            Constants.validateUserMobileInvalid -> {
                til_usermoile.error = getString(R.string.enterValidMobile)
                tie_usermobile.requestFocus()
            }
            Constants.validateDob -> {
                til_userdob.error = getString(R.string.mandatoryField)
                tie_userdob.requestFocus()
            }
        }
    }


    private fun showProgress(show: Boolean = false) {
        AlertMessages.getInstance().displayProgress(this, show)
    }

    fun pickDob() {
        val calendar = Calendar.getInstance()
        val yy = calendar.get(Calendar.YEAR)
        val mm = calendar.get(Calendar.MONTH)
        val dd = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(
            this, R.style.CustomDatePicker,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val date = (dayOfMonth.toString() + "-" + (monthOfYear + 1).toString()
                        + "-" + year.toString())
                val age = registrationViewModel!!.getAge(year, monthOfYear + 1, dayOfMonth)
                if (age >= 1) {
                    tie_userdob.setText(date)
                    tie_userage.setText(age.toString())
                }

            }, yy, mm, dd
        )
        datePicker.datePicker.maxDate = System.currentTimeMillis()
        datePicker.show()
    }

    private fun selectValue(spinner: Spinner, value: String) {
        for (i in 0 until spinner.count) {
            Log.d(TAG, "sri_local: " + spinner.getItemAtPosition(i) + " server: " + value)
            val qualification = spinner.getItemAtPosition(i).toString().toLowerCase()
            if (qualification.equals(value)) {
                spinner.setSelection(i)
                break
            }
        }
    }

    private fun clearForm(group: ViewGroup) {
        var i = 0
        val count = group.childCount
        while (i < count) {
            val view = group.getChildAt(i)
            if (view is EditText) {
                view.setText("")
            }

            if (view is ViewGroup && view.childCount > 0)
                clearForm(view)
            ++i
        }
    }

    fun setLocale(lang: String?) {
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)

    }

    fun reloadApp(){
        val refresh = Intent(this, NavDrawerActivity::class.java)
        refresh.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        finish()
        startActivity(refresh)
    }
}
