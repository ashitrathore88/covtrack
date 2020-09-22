package com.covid.covtrack.views.fragments

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.covid.covtrack.views.activities.BaseActivity
import java.util.*


abstract class BaseFragment : Fragment(){
    companion object {
        public var dLocale: Locale? = null
    }

//    init {
//        updateConfig(activity!!)
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        updateConfig(context)
    }
    fun updateConfig(wrapper: Context) {
        if(BaseFragment.dLocale ==Locale("") ) // Do nothing if dLocale is null
            return

        Locale.setDefault(BaseFragment.dLocale!!)
        val configuration = Configuration()
        configuration.setLocale(BaseFragment.dLocale!!)
        wrapper?.resources?.updateConfiguration(configuration,
            wrapper?.resources!!.displayMetrics)
    }


}