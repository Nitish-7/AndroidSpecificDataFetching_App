package com.app.secquraise.backgroundWorker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.secquraise.R
import com.app.secquraise.repository.DataRepository
import com.app.secquraise.viewmodel.MainViewModel
import javax.inject.Inject

class MyBroadcastReceiver @Inject constructor() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val batteryPercentage = (level.toFloat() / scale.toFloat() * 100).toInt()

                if (batteryPercentage < 20) {
                    sendLowBatteryNotification(context)
                }
            }

        }

    @SuppressLint("ServiceCast")
    private fun sendLowBatteryNotification(context: Context?) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "low_battery_20",
                "Low Battery",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "low_battery_20")
            .setContentTitle("Low Battery Warning")
            .setContentText("Your device battery is less than 20%.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(1, notification)
    }
}
