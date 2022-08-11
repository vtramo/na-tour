package com.example.natour.data.sources.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface TrailApiService {

    @Multipart
    @POST("/trail")
    suspend fun save(
        @Part("idOwner")            idOwner: RequestBody,
        @Part("trailName")          trailName: RequestBody,
        @Part("trailDifficulty")    trailDifficulty: RequestBody,
        @Part("trailDuration")      trailDuration: RequestBody,
        @Part("trailDescription")   trailDescription: RequestBody,
        @Part("routePoints")        routePoints: RequestBody,
        @Part                       image: MultipartBody.Part
    ): Boolean
}