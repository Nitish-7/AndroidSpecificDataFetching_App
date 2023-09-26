package com.app.secquraise.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.secquraise.model.UiData
import com.app.secquraise.util.Constant

@Dao
interface MyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: UiData)

    @Delete
    suspend fun deleteData(data: UiData)

    @Query("SELECT * FROM ${Constant.TABLE_NAME}")
    fun getAllLocalData(): LiveData<List<UiData>>

}