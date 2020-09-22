package com.covid.covtrack.utilities


import com.connect.yourneed.app.ApIInterface
import com.covid.covtrack.app.ApiClient
import com.covid.covtrack.models.*
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


open class NetworkManager {

    val TAG = javaClass.simpleName
    var apiInterface = ApiClient.getClient().create(ApIInterface::class.java);

    fun signup(
        userName: String, userMobile: String, dob: String,
        age: String, address: String, qualification: String,
        bevolunteer: String, islabour: String, pincode: String
    ): Observable<BaseResponse> {
        return apiInterface.ApiSignUp(
            userName,
            userMobile,
            dob,
            age,
            address,
            qualification,
            bevolunteer,
            islabour,
            pincode
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getProfile(userId: String): Observable<UserDetails> {
        return apiInterface.ApiGetProfile(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    fun updateProfile(
        userId: String, userName: String, userMobile: String, dob: String,
        age: String, address: String, qualification: String,
        bevolunteer: String, islabour: String, pincode: String
    ): Observable<BaseResponse> {
        return apiInterface.ApiUpdateProfile(
            userId,
            userName,
            userMobile,
            dob,
            age,
            address,
            qualification,
            bevolunteer,
            islabour,
            pincode
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun login(mobile: String): Observable<BaseResponse> {
        return apiInterface.ApiLogin(mobile)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun verifyOtp(mobile: String, otp: String): Observable<UserDetails> {
        return apiInterface.ApiVerifyOTP(mobile, otp)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun saveOtp(mobile: String, otp: String): Observable<UserDetails> {
        return apiInterface.ApiSaveOTP(mobile, otp)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getCovidStatus(): Observable<CovidStatistics> {
        return apiInterface.ApiCovidStatistics()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getRegions(): Observable<Regions>{
        return apiInterface.ApiRegions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getDoctors(region : String): Observable<Doctors>{
        return apiInterface.ApiDoctors(region)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getHospitals(): Observable<Hospitals>{
        return apiInterface.ApiCovidHospitals()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getAppUpdate(currentVersion: Int): Observable<BaseResponse> {
        return apiInterface.ApiAppUpdate(currentVersion)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getCovidNews(): Observable<CovidNews> {
        return apiInterface.ApiCovidNews()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    fun getCovidGovNews(): Observable<CovidNews> {
        return apiInterface.ApiCovidGovNews()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getCovidVideo(): Observable<CovidNews> {
        return apiInterface.ApiCovidVideo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getCovidQuestions(userId: String): Observable<CovidQuestions> {
        return apiInterface.ApiQuestions(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getCovidMaps(): Observable<CovidOnMap> {
        return apiInterface.ApiOnMap()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun uploadAnswers(jsonObject: JsonObject): Observable<BaseResponse> {
        return apiInterface.ApiUploadAnswers(jsonObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    /*private fun sendDataRetrofit() {
        val alertMessages = AlertMessages()
        alertMessages.showProgressDialog(this)

        val name_str = tie_username.text.toString()
        val mobile_str = tie_user_mobile.text!!.toString()
        val email_str = tie_email.text!!.toString()
        val address_str = tie_address.text!!.toString()
        val password_str = tie_con_password.text!!.toString()


        try {
            val apiInterface = ApiClient.getClient().create(ApIInterface::class.java)
            val call = apiInterface.ApiSignUp(name_str, mobile_str, email_str, address_str, password_str)
            call.enqueue(object : Callback<UserDetails> {
                override fun onResponse(call: Call<UserDetails>, response: Response<UserDetails>) {
                    alertMessages.hideProgressDialog()
                    Log.d(TAG, "sri_ret_response: " + Gson().toJson(response.body()))
                }

                override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                    alertMessages.hideProgressDialog()
                }

            })
        } catch (ex: Exception) {
            alertMessages.hideProgressDialog()
            ex.printStackTrace()
        }
    }*/
}