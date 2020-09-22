package com.covid.covtrack.app

import android.app.Application
import android.content.res.Resources
import androidx.preference.PreferenceManager
import com.covid.covtrack.utilities.PreferenceUtil
import com.covid.covtrack.views.activities.BaseActivity
import com.covid.covtrack.views.fragments.BaseFragment
import java.util.*

class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        res = resources
        var change: String
//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
//        val language = sharedPreferences.getString("language", "en")
        val language =  PreferenceUtil.ins.getValueString("language");
        if (language == "pa") {
            change="pa"
        } else if (language=="en" ) {
            change = "en"
        }else {
            change =""
        }

        BaseActivity.dLocale = Locale(change)
        BaseFragment.dLocale = Locale(change)
    }

    companion object {
        @get:Synchronized
        var instance: AppController? = null
            private set
        @get:Synchronized
        var res: Resources? = null
            private set
    }
}