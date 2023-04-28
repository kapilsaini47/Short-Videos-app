package com.example.shortvideos.utils

sealed class Resource<T>(
    val data:T?= null,
    val message:String?=null
) {
    //Response State handling classes
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()

}