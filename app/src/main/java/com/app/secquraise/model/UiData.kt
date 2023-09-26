package com.app.secquraise.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.secquraise.util.Constant

@Entity(tableName = Constant.TABLE_NAME)
data class UiData(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var internetConnected: String?,
    var batteryCharging: String?,
    var batteryPercentage: String?,
    var location: String?,
    var time: String?
)