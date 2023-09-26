package com.app.secquraise

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.secquraise.databinding.ActivityMainBinding
import com.app.secquraise.util.ApiResponseHandler
import com.app.secquraise.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private val PERMISSION_REQUEST_CODE = 11
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLocationPermission()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = mainViewModel
        bindObservers()
    }

    private fun startPeriodicUiUpdate() {
        CoroutineScope(Dispatchers.Main).launch {
            while (true){
                mainViewModel.updateUidata()
                delay(15 * 60 * 1000)
            }
        }
    }

    fun bindObservers() {
        mainViewModel.allLocalData.observe(this, Observer { data ->
            synchronized(data) {
                for (it in data) {
                    mainViewModel.uploadDataToFirebase(it)
                }
            }
        })

        mainViewModel.firebaseResponse.observe(this, Observer {
            when (it) {
                is ApiResponseHandler.Failure -> {
                    it.dbRef?.setValue(null)
                }
                is ApiResponseHandler.Loading -> {

                }
                is ApiResponseHandler.Success -> {
                    GlobalScope.launch {
                        mainViewModel.deleteDataFromLocal(it.data!!)
                    }
                }
            }
        })
    }

    private fun checkLocationPermission() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            getLocationLatLong()
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLocationLatLong() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener(this) { location ->
                val latLong = location?.let {
                    it.latitude.toString() + " , " + it.longitude.toString()
                }
                mainViewModel.location.value = latLong
                startPeriodicUiUpdate()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocationLatLong()
                } else {

                }
            }
        }
    }

}