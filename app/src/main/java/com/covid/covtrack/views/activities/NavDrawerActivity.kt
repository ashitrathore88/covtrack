package com.covid.covtrack.views.activities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.covid.covtrack.BuildConfig
import com.covid.covtrack.R
import com.covid.covtrack.utilities.Constants
import com.covid.covtrack.utilities.PreferenceUtil
import com.covid.covtrack.viewModels.CovidStatsViewModel
import com.covid.covtrack.views.fragments.*
import com.google.android.material.navigation.NavigationView
import java.util.*


class NavDrawerActivity : BaseActivity() , NavigationView.OnNavigationItemSelectedListener {
    val TAG = javaClass.simpleName
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var title: TextView
    lateinit var localeStr : String
    private lateinit var covidStatsViewModel: CovidStatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        toolbar = findViewById(R.id.toolbar)
        title = toolbar.findViewById(R.id.toolbar_title)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        localeStr = PreferenceUtil.ins.getValueString("language")!!;
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            navView.getMenu().performIdentifierAction(R.id.nav_covid_stats, 0);
        }
        covidStatsViewModel = ViewModelProvider(this).get(CovidStatsViewModel::class.java)
        val currentVersion = BuildConfig.VERSION_CODE
        Log.d(TAG,"CurrentVersion: "+currentVersion)
        covidStatsViewModel.checkAppUpdate(currentVersion)
        covidStatsViewModel.appUpdateRes.observe(this,androidx.lifecycle.Observer {
            version_update_popup(it.message!!)
        })
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        selectDrawerItem(item);

        return true
    }

    fun selectDrawerItem(menuItem: MenuItem) { // Create a new fragment and specify the fragment to show based on nav item clicked

        when (menuItem.itemId) {
         //   R.id.nav_home ->launchActivity(HomeActivity::class.java)
            R.id.nav_covid_stats -> {
                title.text = menuItem.title
                goToFragment(CovidStatsFragment::class.java)
            }
            R.id.nav_news -> {
                title.text = menuItem.title
                goToFragment(NewsTabsFragment::class.java)
            }
            R.id.nav_patients_near_me -> {
                title.text = menuItem.title
                goToFragment(MapViewFragment::class.java)
            }
            R.id.nav_connect_doctor -> {
                title.text = menuItem.title
                goToFragment(ConnectToDoctorFragment::class.java)
            }//covidStatsViewModel.dialNumber("9878455045", this)
            R.id.nav_care_hospitals ->{
                title.text = menuItem.title
                goToFragment(CovidHospitalsFragment::class.java)
            }
            R.id.nav_donate -> {
                title.text = menuItem.title
                goToFragment(DonateToFragment::class.java)
            }//covidStatsViewModel.openBrowser(Constants.URL_DONATE, this)
            R.id.nav_facebook_page -> covidStatsViewModel.openBrowser(Constants.URL_FACEBOOK, this)
            R.id.nav_generate_pass -> covidStatsViewModel.openBrowser(Constants.URL_GENRATE_PASS, this)
            R.id.nav_control_room -> covidStatsViewModel.dialNumber("01672232304", this)
            R.id.nav_awareness ->  navigateMe(Constants.URL_AWARENESS+"?lang="+localeStr,R.string.str_covid_awareness)
            R.id.nav_report_gathering ->launchActivity(ReportGatheringActivity::class.java)
            R.id.nav_faq -> navigateMe(Constants.URL_FAQ+"?lang="+localeStr,R.string.str_faq)
            R.id.nav_quarantine_facility -> covidStatsViewModel.openBrowser(Constants.URL_BOOKING, this)

            R.id.nav_become_volunteer -> navigateMe(Constants.URL_BECOME_VOLUNTEER,R.string.str_become_volunteer)
//            R.id.nav_health_check ->{
//                title.text = menuItem.title
//                goToFragment(HealthCheckFragment::class.java)
//            }
            R.id.nav_update_profile ->{
                val i1 = Intent(this@NavDrawerActivity, RegistrationActivity::class.java)
                i1.putExtra("from", true)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                startActivity(i1)
            }
            R.id.nav_logout -> logOut()
            else -> goToFragment(CovidStatsFragment::class.java)
        }

        // Highlight the selected item has been done by NavigationView
        //menuItem.isChecked = true


        // Close the navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun goToFragment(fragmentClass: Class<*>){
        var fragment: Fragment? = null
        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Insert the fragment by replacing any existing fragment
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment!!).commit()
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

    fun navigateMe(url: String, title : Int) {
        Log.d(TAG,"Naviagte to URL "+url)
        var intent = Intent(this@NavDrawerActivity,NewsWebViewActivity::class.java);
        intent.putExtra("newsId",url)
        intent.putExtra("title",getString(title))
        startActivity(intent)
    }

    private fun launchActivity(activity : Class<*>){

        val i1 = Intent(this,activity)
        startActivity(i1)

    }

    fun logOut() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        builder.setMessage(R.string.str_do_you_want_to_logout)
            .setPositiveButton(R.string.str_yes) { dialog, id ->
                PreferenceUtil.ins.signOut()
                val i1 = Intent(this, LoginActivity::class.java)
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i1)
                finishAffinity()
            }.setNegativeButton(R.string.str_no,
                DialogInterface.OnClickListener { dialogInterface, i -> return@OnClickListener })

        val alertDialog = builder.create()
        alertDialog.show()

    }

    fun performDrawerItemClick(id : Int){
        if (id == Constants.NAVIGATE_HEALTHCHECK) selectDrawerItem(navView.getMenu().getItem(1))
    }
}