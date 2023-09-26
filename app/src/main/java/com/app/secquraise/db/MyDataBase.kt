package com.app.secquraise.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.secquraise.model.UiData

@Database(entities = [UiData::class], version = 3, exportSchema = false)
abstract class MyDataBase: RoomDatabase() {
    abstract fun getDao(): MyDao
}