package com.example.shortvideos.api

import com.example.shortvideos.model.ResponseModel
import com.example.shortvideos.utils.Resource
import com.example.shortvideos.utils.Util.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/api/videos")
    suspend fun getVideos(
        @Query("key")
        key:String,
        @Query("q")
        flower:String
    ):Response<ResponseModel>

    @GET("/api/videos")
    suspend fun getOneVideo(
        @Query("key")
        key:String,
        @Query("id")
        videoId:String
    ):Resource<ResponseModel>


}