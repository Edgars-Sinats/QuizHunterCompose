package com.example.quizhuntercompose.network

import android.media.Image
import coil.request.ImageRequest
import retrofit2.http.GET
import retrofit2.http.Path

interface DriversCApi {

    @GET("bildes/{imagePath}")
    suspend fun getPicture(
        @Path("imagePath") imagePath: String
//        @Query()
    ): Image
}