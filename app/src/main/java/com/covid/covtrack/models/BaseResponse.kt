package com.covid.covtrack.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseResponse {

    @SerializedName("err_code")
    @Expose
    var status: String? = null

    @SerializedName("error_type")
    @Expose
    var errorType: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("id")
    @Expose
    var productId: Int? = null

}
