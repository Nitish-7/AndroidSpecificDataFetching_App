package com.app.secquraise.repository

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.MutableLiveData
import com.app.secquraise.db.MyDao
import com.app.secquraise.model.UiData
import com.app.secquraise.util.SharedPrefData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DataRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val dao:MyDao,
    private val sharedPrefData: SharedPrefData
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val currentUIData = MutableLiveData<UiData>()
    val allLocalData = dao.getAllLocalData()

    val dateTime = MutableLiveData<String>()
    val captureCount = MutableLiveData<String>()
    val locationLiveData = MutableLiveData<String>()
    var frequency = MutableLiveData<String>(sharedPrefData.getFrequency())

    private val internetConnected = MutableLiveData<String>()
    private val batteryCharging = MutableLiveData<String>()
    private val batteryPercentage = MutableLiveData<String>()

    fun deleteDataFromLocal(uiData: UiData) {

    }

    fun upadateUiData() {
        updateFrequency()
        updateInternetConnectedStatus()
        updateBatteryStatus()
        updateTimestamp()
        updateCaptureCount()
        setAllCurentUiData()
    }

    private fun updateFrequency() {
        sharedPrefData.updateFrequency(frequency.value!!)
    }

    private fun updateCaptureCount() {
        sharedPrefData.addNewCaptureCount()
    }

    private fun updateInternetConnectedStatus() {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected = networkInfo?.isConnectedOrConnecting ?: false
        if (isConnected) {
            internetConnected.value = "ON"
        } else {
            internetConnected.value = "OFF"
        }
    }

    private fun updateBatteryStatus() {
        val batteryStatus =
            context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging =
            status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
        if (isCharging) {
            batteryCharging.value = "ON"
        } else {
            batteryCharging.value = "OFF"
        }

        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val percentage = (level / scale.toFloat() * 100).toInt()
        batteryPercentage.value = percentage.toString()
    }


    private fun updateTimestamp() {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currentDateAndTime: String = sdf.format(Date())
        dateTime.value = currentDateAndTime
    }

    fun setAllCurentUiData() {
        val data = UiData(
            captureFrequency =frequency.value,
            internetConnected = internetConnected.value,
            batteryCharging = batteryCharging.value,
            batteryPercentage = batteryPercentage.value,
            location = locationLiveData.value,
            time = dateTime.value
        )
        currentUIData.postValue(data)
        captureCount.postValue(sharedPrefData.getTotalCaptureCount().toString())
        GlobalScope.launch {
            dao.insertData(data)
        }
    }
}
