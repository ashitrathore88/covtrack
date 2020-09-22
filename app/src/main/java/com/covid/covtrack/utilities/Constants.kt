package com.covid.covtrack.utilities

import android.content.res.Resources
import android.os.Build

object Constants {

    const val API_SERVER_URL = "https://covtrack.co.in/api/"
    const val LOGIN = "login_otp"
    const val SIGNUP = "registration"
    const val OTP_VERIFY = "verify_otp"
    const val RESEND_OTP = "resend_otp"
    const val FORGET_PASSWORD = "forgot_password"
    const val COVID_STATISTICS = "covid_statistics"
    const val COVID_NEWS = "news"
    const val COVID_GOV_NEWS = "gov_orders"
    const val COVID_VIDEOS = "videos"
    const val ON_MAP = "patients"
    const val QUESTIONS = "questions"
    const val USER_DETAILS = "user_details"
    const val UPDATE_PROFILE = "update_user"
    const val SUMBIT_ANSWERS = "submit_answers"
    const val SUB_DIVISION_LIST = "sub_division_id"
    const val APP_UPDATE = "check_for_update_user"
    const val DOCTORS = "doctors"
    const val REGIONS = "regions"
    const val HOSPITALS = "hospitals"
    const val SAVE_OTP = "save_otp"


    const val USER_NAME = "username"
    const val USER_ID = "userid"
    const val USER_MOBILE = "usermobile"
    const val GRAPH_IMG = "graphImg"

    const val COVID_HOSPITALS = "Covid Hospital"
    const val COVID_HOTSPOTS = "HotSpot"
    const val COVID_TEST_CENTERS = "Covid Test Center"
    const val COVID_PATIENTS = "Patient"

    const val SRT_YES = "yes"
    const val SRT_NO = "no"
    const val SRT_ENG = "EN"
    const val SRT_PA = "PA"

    const val URL_FAQ = "http://covtrack.co.in/pages/faq/faq/faq.amp.html"//"https://www.cdc.gov/coronavirus/2019-ncov/faq.html"
    const val URL_INFO = "http://covtrack.co.in/pages/faq/faq/info.amp.html"
    const val URL_BOOKING = "http://covidhelp.punjab.gov.in/HotelBookingTermandConditions.aspx"
    const val URL_BECOME_VOLUNTEER = "https://docs.google.com/forms/d/e/1FAIpQLSfyaj46iEyk1uVpPeTYN5EuhPiM2iqjgptbAB4leu6il6KTeg/viewform"
    const val URL_DONATE = "https://cmrf.punjab.gov.in/"
    const val URL_GENRATE_PASS = "https://epasscovid19.pais.net.in/"
    const val URL_AWARENESS = "http://covtrack.co.in/pages/awareness/awareness.html"
    const val URL_FACEBOOK = "https://www.facebook.com/DistrictAdministrationSangrur/"

    const val NAVIGATE_HOMEFRAG = 1141
    const val NAVIGATE_UPDATEAPP = 1142
    const val NAVIGATE_HEALTHCHECK = 1143
    const val  APiKEY = "AIzaSyBng0WjQPXgQqpqHwNNek5A_lRFR1UjzlQ"
    const val  PLUS = "+"
    const val  PHONECODE = "91"
    const val  NEWS_CONTENT = "NEWS_CONTENT"



    //const validation message for registration
    const val validateUserName = "validateUserName"
    const val validateUserMobile = "validateUserMobile"
    const val validateUserMobileInvalid = "validateUserMobileInvalid"
    const val validationSuccess = "validaationSuccess"
    const val validateOTP = "validOTP"
    const val validateOTPInvalid = "validateOTPInvalid"
    const val validateDob = "validateDob"
    const val validateQualification = "validateQualification"


    //ErrorMessage
    const val ErrorMsg = "Server Not Responding"

    fun dp2px(dp: Int): Float = dp * Resources.getSystem().displayMetrics.density

    fun isLollipop() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

}
