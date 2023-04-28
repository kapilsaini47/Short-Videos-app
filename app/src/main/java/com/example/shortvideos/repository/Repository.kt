package com.example.shortvideos.repository

import com.example.shortvideos.api.RetrofitInstance
import com.example.shortvideos.utils.Util.Companion.API_KEY

class Repository {

    suspend fun getVideos(q:String) =
        RetrofitInstance.api.getVideos(API_KEY,q)

    suspend fun getOneVideo(id:String) = RetrofitInstance.api.getOneVideo(API_KEY,id)

}