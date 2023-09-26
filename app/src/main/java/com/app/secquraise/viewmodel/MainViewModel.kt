package com.app.secquraise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.secquraise.db.MyDao
import com.app.secquraise.model.UiData
import com.app.secquraise.repository.DataRepository
import com.app.secquraise.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val firebaseRepository: FirebaseRepository,
) : ViewModel() {
    val currentUiData = dataRepository.currentUIData
    val allLocalData =dataRepository.allLocalData

    val dateTime = dataRepository.dateTime
    val captureCount = dataRepository.captureCount
    val location=dataRepository.locationLiveData
    var frequency = MutableLiveData<String>("15")
    val firebaseResponse=firebaseRepository.firebaseResponse

    fun updateUidata() {
        dataRepository.upadateUiData()
    }

    fun uploadDataToFirebase(uiData: UiData){
        firebaseRepository.uploadDataToFirebase(uiData)
    }

    fun deleteDataFromLocal(uiData: UiData) {
        dataRepository.deleteDataFromLocal(uiData)
    }
}
