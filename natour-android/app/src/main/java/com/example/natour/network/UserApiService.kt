package com.example.natour.network

import com.example.natour.data.dto.TrailDto
import com.example.natour.network.util.URLs.AUTHORIZATION_HEADER
import retrofit2.http.*

interface UserApiService {
    @FormUrlEncoded
    @POST("/user")
    suspend fun existsByUsername(@Field("username") username: String): Boolean

    @Headers("Content-Type: application/json")
    @PUT("user/trail/favorite/{userId}")
    suspend fun addFavoriteTrail(
        @Body trailId: Long,
        @Path("userId") userId: Long,
        @Header(AUTHORIZATION_HEADER) authHeader: String
    ): Boolean

    @HTTP(method = "DELETE", path = "user/trail/favorite/{userId}", hasBody = true)
    suspend fun removeFavoriteTrail(
        @Body trailId: Long,
        @Path("userId") userId: Long,
        @Header(AUTHORIZATION_HEADER) authHeader: String
    ): Boolean

    @GET("user/trail/favorite/{userId}")
    suspend fun getFavoriteTrails(
        @Path("userId") userId: Long,
        @Header(AUTHORIZATION_HEADER) authHeader: String
    ): List<TrailDto>
}