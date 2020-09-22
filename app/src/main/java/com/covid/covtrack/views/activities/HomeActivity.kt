package com.covid.covtrack.views.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import com.covid.covtrack.R
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.*
import com.covid.covtrack.BuildConfig
import com.covid.covtrack.utilities.Constants.NAVIGATE_HOMEFRAG
import com.covid.covtrack.utilities.Constants.URL_FAQ
import com.covid.covtrack.utilities.Constants.URL_INFO
import com.covid.covtrack.utilities.PreferenceUtil
import com.covid.covtrack.viewModels.CovidStatsViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*


class HomeActivity : BaseActivity() {
    val TAG = javaClass.simpleName
    var nav_covidstats: View? = null
    var nav_healthcheck: View? = null
    var nav_news: View? = null
    var nav_covidmap: View? = null
    var appBarConfiguration: AppBarConfiguration? = null
    private lateinit var covidStatsViewModel: CovidStatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_covidstats,
                R.id.navigation_news_tab,
                R.id.navigation_map,
                R.id.navigation_healthcheck
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration!!)
        navView.setupWithNavController(navController)

        nav_healthcheck = navView.findViewById(R.id.navigation_healthcheck)
        nav_news = navView.findViewById(R.id.navigation_news_tab)
        nav_covidmap = navView.findViewById(R.id.navigation_map)
        nav_covidstats = navView.findViewById(R.id.navigation_covidstats)

        covidStatsViewModel = ViewModelProvider(this).get(CovidStatsViewModel::class.java)
        val currentVersion = BuildConfig.VERSION_CODE
        Log.d(TAG,"CurrentVersion: "+currentVersion)
        covidStatsViewModel.checkAppUpdate(currentVersion)
        covidStatsViewModel.appUpdateRes.observe(this,androidx.lifecycle.Observer {
            version_update_popup(it.message!!)
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_profile) {
            val i1 = Intent(this@HomeActivity, RegistrationActivity::class.java)
            i1.putExtra("from", true)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            startActivity(i1)
            return true
        } else if (id == R.id.action_info) {
            navigateMe(URL_INFO)
            return true
        } else if (id == R.id.action_faq) {
            navigateMe(URL_FAQ)
            return true
        } else if (id == R.id.action_logout) {
            logOut()
        }

        return item.onNavDestinationSelected(this.findNavController(R.id.nav_host_fragment)) || super.onOptionsItemSelected(
            item
        )
    }

    //back navigation
    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return NavigationUI.navigateUp(
            navController,
            appBarConfiguration!!
        ) || super.onSupportNavigateUp()
    }




    fun clickBottomItem(id: Int) {
        if (id == R.id.btn_healthcheck) nav_healthcheck!!.performClick()
        else if (id == R.id.btn_news) nav_news!!.performClick()
        else if (id == R.id.btn_map) nav_covidmap!!.performClick()
        else if (id == NAVIGATE_HOMEFRAG) nav_covidstats!!.performClick()
        else if (id == NAVIGATE_HOMEFRAG) nav_covidstats!!.performClick()
    }


    fun logOut() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        builder.setMessage("Do you want to logout?")
            .setPositiveButton("Yes") { dialog, id ->
                PreferenceUtil.ins.signOut()
                val i1 = Intent(this, LoginActivity::class.java)
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i1)
                finishAffinity()
            }.setNegativeButton("No",
                DialogInterface.OnClickListener { dialogInterface, i -> return@OnClickListener })

        val alertDialog = builder.create()
        alertDialog.show()

    }

    fun navigateMe(url: String) {
//        val bundle = Bundle()
//        bundle.putString("newsId", url)
//        Navigation.findNavController(this, R.id.nav_host_fragment)
//            .navigate(R.id.action_navigation_covidstats_to_newsViewFragment, bundle)
        var intent = Intent(this@HomeActivity,NewsWebViewActivity::class.java);
        intent.putExtra("newsId",url)
        intent.putExtra("title",getString(R.string.str_faq))
        startActivity(intent)
    }

    fun version_update_popup(message: String) {
        val builder = AlertDialog.Builder(this)
        val layoutInflater = this.layoutInflater
        @SuppressLint("InflateParams") val customView =
            layoutInflater.inflate(R.layout.popup_message, null)
        val tv_header = customView.findViewById<TextView>(R.id.tv_header)
        val tv_info = customView.findViewById<TextView>(R.id.tv_info)
        val btn_ok = customView.findViewById<Button>(R.id.btn_ok)

        val btn_text = "Update"
        btn_ok.setText(btn_text)
        btn_ok.setAllCaps(true)

        val title = "Update Available"
        tv_header.setText(title)
        tv_info.setText(message)
        builder.setView(customView)
        builder.setCancelable(true)
        val ad = builder.create()

        ad.show()
        Objects.requireNonNull(ad.window)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btn_ok.setOnClickListener(View.OnClickListener {
            val facebookAppIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.covid.covtrack")
            )
            startActivity(facebookAppIntent)
        })

    }


    fun set_snack_bar(message: String) {
        val snackbar =
            Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}
