package com.covid.covtrack.utilities


import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.covid.covtrack.app.AppController
import com.covid.covtrack.utilities.Constants.USER_ID


class PreferenceUtil(prefObj: SharedPreferences) {

    var sharedPref: SharedPreferences? = prefObj

    companion object {
        private var pref: PreferenceUtil? = null

        val ins: PreferenceUtil
            @Synchronized get() {
                if (pref == null) {
                    pref = PreferenceUtil(
                        PreferenceManager.getDefaultSharedPreferences(
                            AppController.instance
                        )
                    )
                }
                return pref!!
            }
    }

    fun saveValue(KEY_NAME: String, value: Int) {
        sharedPref!!.edit().putInt(KEY_NAME, value).apply()
    }

    fun saveValue(KEY_NAME: String, value: String) {
        sharedPref!!.edit().putString(KEY_NAME, value).apply()
    }

    fun saveValue(KEY_NAME: String, value: Boolean) {
        sharedPref!!.edit().putBoolean(KEY_NAME, value).apply()
    }

    fun getValueString(KEY_NAME: String): String? {
        return sharedPref!!.getString(KEY_NAME, "")
    }

    fun getValueInt(KEY_NAME: String): Int {
        return sharedPref!!.getInt(KEY_NAME, 0)
    }

    fun getValueBoolean(KEY_NAME: String): Boolean? {
        return sharedPref!!.getBoolean(KEY_NAME, false)
    }

    fun removeValue(KEY_NAME: String) {
        sharedPref!!.edit().remove(KEY_NAME).apply()
    }

    fun clearSharedPreference() {
        sharedPref!!.edit().clear().apply()
    }

    fun signOut() {
        val editor: SharedPreferences.Editor = sharedPref!!.edit()
        editor.putString(USER_ID, null)
        editor.clear()
        editor.apply()
    }

}
