package com.connect.yourneed.app

import com.covid.covtrack.models.*
import com.covid.covtrack.utilities.Constants.APP_UPDATE
import com.covid.covtrack.utilities.Constants.COVID_GOV_NEWS
import com.covid.covtrack.utilities.Constants.COVID_NEWS
import com.covid.covtrack.utilities.Constants.COVID_STATISTICS
import com.covid.covtrack.utilities.Constants.COVID_VIDEOS
import com.covid.covtrack.utilities.Constants.DOCTORS
import com.covid.covtrack.utilities.Constants.HOSPITALS
import com.covid.covtrack.utilities.Constants.LOGIN
import com.covid.covtrack.utilities.Constants.ON_MAP
import com.covid.covtrack.utilities.Constants.OTP_VERIFY
import com.covid.covtrack.utilities.Constants.QUESTIONS
import com.covid.covtrack.utilities.Constants.REGIONS
import com.covid.covtrack.utilities.Constants.SAVE_OTP
import com.covid.covtrack.utilities.Constants.SIGNUP
import com.covid.covtrack.utilities.Constants.SUMBIT_ANSWERS
import com.covid.covtrack.utilities.Constants.UPDATE_PROFILE
import com.covid.covtrack.utilities.Constants.USER_DETAILS
import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.*

interface ApIInterface {
    @FormUrlEncoded
    @POST(SIGNUP)
    fun ApiSignUp(
        @Field("name") name: String,
        @Field("mobile") mobile: String,
        @Field("dob") dob: String,
        @Field("age") age: String,
        @Field("address") address: String,
        @Field("qulification") qulification: String,
        @Field("be_volunteer_intrested") be_volunteer_intrested: String,
        @Field("is_labour") is_labour: String,
        @Field("pincode") pincode: String
    ): Observable<BaseResponse>

    @FormUrlEncoded
    @POST(OTP_VERIFY)
    fun ApiVerifyOTP(
        @Field("mobile") mobile: String,
        @Field("otp") otp: String
    ): Observable<UserDetails>

    @FormUrlEncoded
    @POST(SAVE_OTP)
    fun ApiSaveOTP(
        @Field("mobile") mobile: String,
        @Field("otp") otp: String
    ): Observable<UserDetails>
    @FormUrlEncoded
    @POST(LOGIN)
    fun ApiLogin(@Field("mobile") name: String): Observable<BaseResponse>

    @FormUrlEncoded
    @POST(USER_DETAILS)
    fun ApiGetProfile(
        @Field("user_id") userid: String
    ): Observable<UserDetails>

    @FormUrlEncoded
    @POST(UPDATE_PROFILE)
    fun ApiUpdateProfile(
        @Field("user_id") userid: String,
        @Field("name") name: String,
        @Field("mobile") mobile: String,
        @Field("dob") dob: String,
        @Field("age") age: String,
        @Field("address") address: String,
        @Field("qulification") qulification: String,
        @Field("be_volunteer_intrested") be_volunteer_intrested: String,
        @Field("is_labour") is_labour: String,
        @Field("pincode") pincode: String
    ): Observable<BaseResponse>

    @FormUrlEncoded
    @POST(QUESTIONS)
    fun ApiQuestions(@Field("user_id") userid: String): Observable<CovidQuestions>

    @Headers("Content-type: application/json")
    @POST(SUMBIT_ANSWERS)
    fun ApiUploadAnswers(@Body model: JsonObject): Observable<BaseResponse>


    @GET(COVID_STATISTICS)
    fun ApiCovidStatistics(): Observable<CovidStatistics>

    @GET(REGIONS)
    fun ApiRegions(): Observable<Regions>

    @FormUrlEncoded
    @POST(DOCTORS)
    fun ApiDoctors(@Field("region_id") region: String): Observable<Doctors>

    @GET(HOSPITALS)
    fun ApiCovidHospitals(): Observable<Hospitals>

    @GET(COVID_NEWS)
    fun ApiCovidNews(): Observable<CovidNews>

    @GET(COVID_GOV_NEWS)
    fun ApiCovidGovNews(): Observable<CovidNews>

    @GET(COVID_VIDEOS)
    fun ApiCovidVideo(): Observable<CovidNews>

    @GET(ON_MAP)
    fun ApiOnMap(): Observable<CovidOnMap>

    @FormUrlEncoded
    @POST(APP_UPDATE)
    fun ApiAppUpdate(@Field("current_version")currentVersion: Int): Observable<BaseResponse>
}
