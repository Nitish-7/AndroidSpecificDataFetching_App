package com.app.secquraise

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import com.app.secquraise.backgroundWorker.MyBroadcastReceiver
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application(){
    @Inject
    lateinit var myBroadcastReceiver: MyBroadcastReceiver
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        val intentFilter=IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED)
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)

        registerReceiver(myBroadcastReceiver, intentFilter)
    }
}