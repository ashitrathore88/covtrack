package com.covid.covtrack.views.fragments


import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.covid.covtrack.R
import com.covid.covtrack.models.CovidOnMap
import com.covid.covtrack.utilities.AlertMessages
import com.covid.covtrack.utilities.Constants.COVID_HOSPITALS
import com.covid.covtrack.utilities.Constants.COVID_HOTSPOTS
import com.covid.covtrack.utilities.Constants.COVID_PATIENTS
import com.covid.covtrack.utilities.Constants.COVID_TEST_CENTERS
import com.covid.covtrack.viewModels.MapsViewModel
import com.covid.covtrack.viewModels.NewsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_news.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.annotation.DrawableRes
import com.google.android.gms.maps.model.BitmapDescriptor
import android.graphics.Canvas


class MapViewFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mapsViewModel: MapsViewModel
    internal var fragmentManager: FragmentManager? = null
    internal var lat_long = LatLng(17.6868, 83.21852)
    private val marker: Marker? = null
    private var mMap: GoogleMap? = null
    var mapsList: ArrayList<CovidOnMap.MapData>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mapsViewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_map_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapsViewModel.getCovidMaps()
        mapsViewModel.covidMapsResponse.observe(this, Observer {

            val status = it.status
            if (status.equals("valid")) {
                mapsList = ArrayList<CovidOnMap.MapData>()
                mapsList!!.addAll(it.data)
                loadmap_view()
            } else {
                tv_no_records.visibility = View.VISIBLE
                if (activity != null) AlertMessages.getInstance().alertMsgBox(
                    it.message!!,
                    activity!!
                )
            }
        })

        mapsViewModel.progressDialog.observe(this, Observer {
            if (activity != null) showProgress(it)
        })

        mapsViewModel.alertMessage.observe(this, Observer {
            if (activity != null) AlertMessages.getInstance().alertMsgBox(it, activity!!)
        })
    }

    private fun loadmap_view() {
        fragmentManager = this.childFragmentManager
        val mapFragment = fragmentManager!!.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        for (i in mapsList!!.indices) {
            val model = mapsList!!.get(i)
            val pointsList = model.patients
            val name = model.name
            for (point in pointsList) {
                var title = point.title
                val latString = point.latitude
                val longString = point.longitude
                if (latString == "" || latString.isEmpty() && longString == "" || longString.isEmpty()) {
                    return
                }
                val longDouble = java.lang.Double.parseDouble(longString)
                val lattDouble = java.lang.Double.parseDouble(latString)
                val lat_long = LatLng(lattDouble, longDouble)

                val options = MarkerOptions()
                options.position(lat_long)
                options.title(name)
                options.snippet(title)

                if (name.equals(COVID_HOSPITALS)) options.icon(bitmapDescriptorFromVector(R.drawable.ic_hospital))
                if (name.equals(COVID_HOTSPOTS)) options.icon(bitmapDescriptorFromVector(R.drawable.ic_hotspot))
                if (name.equals(COVID_TEST_CENTERS)) options.icon(bitmapDescriptorFromVector(R.drawable.ic_test_centers))
                if (name.equals(COVID_PATIENTS)) options.icon(bitmapDescriptorFromVector(R.drawable.ic_patients))

                val marker = mMap!!.addMarker(options)
                mMap!!.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                    override fun getInfoWindow(marker: Marker): View? {
                        return null
                    }

                    override fun getInfoContents(marker: Marker): View {
                        val context = context //or getActivity(), YourActivity.this, etc.
                        val info = LinearLayout(context)
                        info.orientation = LinearLayout.VERTICAL
                        val title = TextView(context)
                        title.setTextColor(Color.BLACK)
                        title.gravity = Gravity.CENTER
                        title.setTypeface(null, Typeface.BOLD)
                        title.text = marker.title

                        val snippet = TextView(context)
                        snippet.setTextColor(Color.GRAY)
                        snippet.text = marker.snippet

                        info.addView(title)
                        info.addView(snippet)
                        return info
                    }
                })
                //mMap!!.addMarker(MarkerOptions().position(lat_long).title(address).snippet(description).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_merchant)));

                mMap!!.setOnMarkerClickListener(this)
                val builder = LatLngBounds.Builder()
                builder.include(marker.position)
                mMap!!.setOnMapLoadedCallback(GoogleMap.OnMapLoadedCallback {
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 16))
                    mMap!!.animateCamera(CameraUpdateFactory.zoomTo(10f))
                })


            }
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(lat_long))
        val zoomLevel = 16.0.toFloat() //This goes up to 21
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(lat_long, zoomLevel))
        return false
    }

    private fun showProgress(show: Boolean = false) {
        AlertMessages.getInstance().displayProgress(activity!!, show)
    }

    private fun bitmapDescriptorFromVector(@DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(activity!!, vectorDrawableResourceId)
        vectorDrawable!!.setBounds(40, 20, vectorDrawable.intrinsicWidth + 40, vectorDrawable.intrinsicHeight + 20)
        val bitmap = Bitmap.createBitmap(150,150, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }



}
