package com.example.submissioninterappstory.location

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.submissioninterappstory.R
import com.example.submissioninterappstory.api.ListAllStoriesItem
import com.example.submissioninterappstory.databinding.ActivityMapsBinding
import com.example.submissioninterappstory.main.MainActivity
import com.example.submissioninterappstory.main.MainViewModel
import com.example.submissioninterappstory.model.ModelFactory
import com.example.submissioninterappstory.model.UserPref
import com.example.submissioninterappstory.welcome.WelcomeScreen

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var arrayList: ArrayList<ListAllStoriesItem>
    private var isFirst: Boolean = false
    private lateinit var viewModel: MapsViewModel
    private lateinit var mainModelAkun: MainViewModel


            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arrayList = ArrayList()

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mainModelAkun = ViewModelProvider(
                    this, ModelFactory(UserPref.getInstance(dataStore))
                )[MainViewModel::class.java]

        viewModel = ViewModelProvider(this )[MapsViewModel::class.java]

        mainModelAkun.getUserakun().observe(this){ user -> viewModel.setLocation(tokenAuth = user.token)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        viewModel.getStoryMaps().observe(this){
            for (stories in it ){
                if (stories.lat != null && stories.lon != null){
                    val position = LatLng(stories.lat, stories.lon)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(stories.name)
                            .snippet(stories.description)
                    )
                    if (!isFirst){
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
                        isFirst = true

                    }
                }
            }

        }



        getMyLocation()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}