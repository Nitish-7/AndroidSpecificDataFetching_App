package com.app.secquraise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.secquraise.db.MyDao
import com.app.secquraise.model.UiData
import com.app.secquraise.repository.DataRepository
import com.app.secquraise.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val firebaseRepository: FirebaseRepository,
) : ViewModel() {
    val currentUiData = dataRepository.currentUIData
    val allLocalData = dataRepository.allLocalData

    val dateTime = dataRepository.dateTime
    val captureCount = dataRepository.captureCount
    val location = dataRepository.locationLiveData
    var frequency = dataRepository.frequency
    val frequencyError = MutableLiveData<String>()
    val firebaseResponse = firebaseRepository.firebaseResponse
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun updateUidata() {
        dataRepository.upadateUiData()
    }

    fun uploadDataToFirebase(uiData: UiData) {
        firebaseRepository.uploadDataToFirebase(uiData)
    }

    fun deleteDataFromLocal(uiData: UiData) {
        dataRepository.deleteDataFromLocal(uiData)
    }

    fun startPeriodicUiUpdate() {
        if (validateFrequency()) {
            coroutineScope.launch {
                while (true) {
                    updateUidata()
                    val frequencyMin = frequency.value?.toLong()!!
                    delay(frequencyMin * 60 * 1000)
                }
            }
        }
    }

    fun validateFrequency(): Boolean {
        frequency.value?.let {
            if (it.isEmpty()) {
                frequencyError.postValue("Please provide the some frequency value")
                return false
            } else if (it.isNotEmpty() && it.toInt() < 1 || it.toInt() > 15) {
                frequencyError.postValue("frequency value should be between 1 to 15 (min)")
                return false
            } else if (it.isNotEmpty() && it.toInt() > 0 && it.toInt() <= 15) {
                frequency.value = it
                frequencyError.postValue("")
                return true
            } else {
                return false
            }
        }
        return false
    }
}
