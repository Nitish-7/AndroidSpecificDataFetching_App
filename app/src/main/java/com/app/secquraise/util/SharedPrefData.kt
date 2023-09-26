package com.app.secquraise.util

import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefData @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreference =
        context.getSharedPreferences(Constant.SHARED_PREF_FILE, Context.MODE_PRIVATE)

    fun addNewCaptureCount() {
        val editor = sharedPreference.edit()
        editor.putInt(Constant.CAPTURE_COUNT, getTotalCaptureCount() + 1)
        editor.apply()
    }

    fun getTotalCaptureCount(): Int {
        return sharedPreference.getInt(Constant.CAPTURE_COUNT, 0)
    }

    fun setUserId(id: String) {
        val editor = sharedPreference.edit()
        editor.putString(Constant.USER_ID, id)
        editor.apply()
    }

    fun getUserId(): String {
        var userId = sharedPreference.getString(Constant.USER_ID, null)
        if (userId == null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference()
            userId = databaseReference.push().key
            userId?.let { setUserId(it) }
        }
        return userId!!
    }

    fun updateFrequency(value: String) {
        val editor = sharedPreference.edit()
        editor.putString(Constant.CAPTURE_FREQUENCY, value)
        editor.apply()
    }
    fun getFrequency(): String {
        var frequency = sharedPreference.getString(Constant.CAPTURE_FREQUENCY, null)
        if (frequency == null) {
            frequency="1"
            updateFrequency(frequency)
        }
        return frequency
    }
}