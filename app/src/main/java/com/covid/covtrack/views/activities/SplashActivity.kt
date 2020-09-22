package com.covid.covtrack.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.covid.covtrack.R
import com.covid.covtrack.utilities.Constants
import com.covid.covtrack.utilities.Constants.USER_ID
import com.covid.covtrack.utilities.PreferenceUtil
import java.lang.Exception

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val user_ID = PreferenceUtil.ins.getValueString(USER_ID)
        if (user_ID!!.isNotEmpty()) {
            Handler().postDelayed({
               // startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                startActivity(Intent(this@SplashActivity, NavDrawerActivity::class.java))
                finish()
            }, 2300)
        } else {
            val thread = object : Thread() {
                override fun run() {
                    try {
                        Thread.sleep(2300)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        val i1 = Intent(this@SplashActivity, LoginActivity::class.java)
                        startActivity(i1)
                        finish()
                    }
                }
            }
            thread.start()
        }
    }
}
