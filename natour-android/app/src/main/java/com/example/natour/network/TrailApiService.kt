package com.example.natour.network

import com.example.natour.data.dto.TrailDto
import com.example.natour.data.dto.TrailReviewDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

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
        @Part                       image: MultipartBody.Part,
        @Header("Authorization")    authHeader: String
    ): Boolean

    @GET("/trail/{page}")
    suspend fun getTrails(
        @Path("page") page: Int,
        @Header("Authorization") authHeader: String
    ): List<TrailDto>

    @Multipart
    @POST("/trail/photo")
    suspend fun addPhoto(
        @Part("idOwner")    idOwner: RequestBody,
        @Part("idTrail")    idTrail: RequestBody,
        @Part("position")   position: RequestBody,
        @Part               image: MultipartBody.Part,
        @Header("Authorization")    authHeader: String
    ): Boolean

    @POST("/trail/review")
    suspend fun addReview(
        @Body trailReview: TrailReviewDto,
        @Header("Authorization") authHeader: String
    ): Boolean
}