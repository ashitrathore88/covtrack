package com.covid.covtrack.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserDetails : BaseResponse() {

    @SerializedName("data")
    @Expose
    var data: LoginData? = null

    class LoginData {
        @SerializedName("id")
        @Expose
        val id: String? = null
        @SerializedName("name")
        @Expose
        val name: String? = null
        @SerializedName("mobile")
        @Expose
        val mobile: String? = null
        @SerializedName("dob")
        @Expose
        val dob: String? = null
        @SerializedName("age")
        @Expose
        val age: String? = null
        @SerializedName("address")
        @Expose
        val address: String? = null

        @SerializedName("pincode")
        @Expose
        val pincode: String? = null

        @SerializedName("is_labour")
        @Expose
        val is_labour: String? = null

        @SerializedName("be_volunteer_intrested")
        @Expose
        val is_volunteer: String? = null

        @SerializedName("qulification")
        @Expose
        val qualification: String? = null

        @SerializedName("device_id")
        @Expose
        val deviceId: Any? = null
        @SerializedName("is_active_type")
        @Expose
        val isActiveType: String? = null
    }
}




