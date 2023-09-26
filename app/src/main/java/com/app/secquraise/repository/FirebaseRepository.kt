package com.app.secquraise.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.secquraise.db.MyDao
import com.app.secquraise.model.UiData
import com.app.secquraise.util.ApiResponseHandler
import com.app.secquraise.util.Constant
import com.app.secquraise.util.SharedPrefData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: MyDao,
    private val sharedPrefData: SharedPrefData
) {
    val firebaseResponse= MutableLiveData<ApiResponseHandler<UiData>>()
    private var retryAttempts = 0
    private var retryDelay = Constant.INITIAL_RETRY_DELAY_MS

    private val databaseReference =
        FirebaseDatabase.getInstance().getReference("users").child(sharedPrefData.getUserId()).child("uiCaptureData")

    fun uploadDataToFirebase(data: UiData){
        firebaseResponse.postValue(ApiResponseHandler.Loading())
        val dataRef = databaseReference.child(data.id.toString())
        insertOneCapture(dataRef,data)
    }

    private fun insertOneCapture(dataRef: DatabaseReference, data: UiData) {
        dataRef.setValue(data)
            .addOnSuccessListener(OnSuccessListener {
                GlobalScope.launch {
                    firebaseResponse.postValue(ApiResponseHandler.Success(data))
                }
            }).addOnFailureListener(OnFailureListener {
                retryDataUpload(dataRef,data)
            })
    }

    private fun retryDataUpload(dataRef: DatabaseReference, data: UiData) {
        if (retryAttempts < Constant.MAX_RETRY_ATTEMPTS) {
            CoroutineScope(Dispatchers.Default).launch {
                delay(retryDelay)
                withContext(Dispatchers.IO) {
                    insertOneCapture(dataRef,data)
                }
                retryAttempts++
                retryDelay *= 2
                if (retryDelay > Constant.MAX_RETRY_DELAY_MS) {
                    retryDelay = Constant.MAX_RETRY_DELAY_MS
                }
            }
        }
        firebaseResponse.postValue(ApiResponseHandler.Failure(dataRef))
    }
}
