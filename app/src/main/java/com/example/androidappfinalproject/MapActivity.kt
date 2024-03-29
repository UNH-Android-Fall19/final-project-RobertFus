package com.example.androidappfinalproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_store.*
import models.Beers
import models.Stores
import java.io.IOException
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.location.Address
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.GeoPoint


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL = (10 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    internal lateinit var mLastLocation: Location
    internal lateinit var mLocationResult: LocationRequest
    private var db: FirebaseFirestore? = null

    private var latitude = 0.0
    private var longitude = 0.0
    private lateinit var mGoogleMap: GoogleMap

//--------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_map)
        val searchAddress = intent.getStringExtra("storeAddress")
        Toast.makeText(this, "Unsuccessful Login $searchAddress", Toast.LENGTH_SHORT).show()
        val searchAddress1 = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$searchAddress"))
        startActivity(searchAddress1)
    //Builds code to send map to fragment
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

//--------------------------------------------------------------------------------------------------
    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }

//--------------------------------------------------------------------------------------------------
    //Sets up google maps to appear.
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mGoogleMap.isMyLocationEnabled = true
            }
        } else {
            mGoogleMap.isMyLocationEnabled = true
        }
        mGoogleMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    latitude,
                    longitude
                )
            ).title("Current Location")
        )
    }

//--------------------------------------------------------------------------------------------------
    //Google Location Services
    protected fun startLocationUpdates() {
        // initialize location request object
        mLocationRequest = LocationRequest.create()
        mLocationRequest!!.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = UPDATE_INTERVAL
            setFastestInterval(FASTEST_INTERVAL)
        }
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient!!.checkLocationSettings(locationSettingsRequest)
        registerLocationListner()
    }

//--------------------------------------------------------------------------------------------------
    private fun registerLocationListner() {
        // initialize location callback object
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                onLocationChanged(locationResult!!.lastLocation)
            }
        }
        if (Build.VERSION.SDK_INT >= 23 && checkPermission()) {
            LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper())
        }
    }

//--------------------------------------------------------------------------------------------------
    private fun onLocationChanged(location: Location) {
        // create message for toast with updated latitude and longitudefa
        var msg = "Updated Location: " + location.latitude + " , " + location.longitude

        // show toast message with updated location
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        val location = LatLng(location.latitude, location.longitude)
        mGoogleMap.clear()
        mGoogleMap.addMarker(MarkerOptions().position(location).title("Current Location"))
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11f))
    }

//--------------------------------------------------------------------------------------------------
    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true;
        } else {
            requestPermissions()
            return false
        }
    }

//--------------------------------------------------------------------------------------------------
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf("Manifest.permission.ACCESS_FINE_LOCATION"),
            1
        )
    }
    
//--------------------------------------------------------------------------------------------------
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION) {
                registerLocationListner()
            }
        }
    }
}
