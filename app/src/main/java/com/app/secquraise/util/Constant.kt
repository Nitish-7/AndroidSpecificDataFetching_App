package com.app.secquraise.util

class Constant {
    companion object {
        const val CAPTURE_FREQUENCY = "captureFrequency"
        const val USER_ID="userId"
        const val LOCATION_REFRESH_TIME=15000L
        const val LOCATION_REFRESH_DISTANCE=10f
        const val FIREBASE_REF="https://secquraise-b769e-default-rtdb.firebaseio.com/"
        const val MAX_RETRY_ATTEMPTS = 5
        const val INITIAL_RETRY_DELAY_MS = 1000L
        const val MAX_RETRY_DELAY_MS = 60000L
        const val CAPTURE_COUNT = "Capture Count"

        const val DATABASE_NAME = "myDb"
        const val TABLE_NAME = "uiDataTable"
        const val FREQUENCY = "Frequency"
        const val CONNECTIVITY = "Connectivity"
        const val BATTERY_PER = "Battery Charge"

        const val CHARGING_STATUS = "Battery Charging"
        const val LOCATION = "Location"

        const val SHARED_PREF_FILE="mySharedPref"
    }
}