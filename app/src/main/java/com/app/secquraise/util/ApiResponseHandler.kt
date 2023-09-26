package com.app.secquraise.util

import com.google.firebase.database.DatabaseReference

sealed class ApiResponseHandler<T>(val data: T? = null, val dbRef:DatabaseReference? = null) {
    class Success<T>(data: T) : ApiResponseHandler<T>(data = data)
    class Failure<T>(dbRef: DatabaseReference) : ApiResponseHandler<T>(dbRef = dbRef)
    class Loading<T>() : ApiResponseHandler<T>()
}