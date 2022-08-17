package com.example.natour.data.sources.network

import retrofit2.http.*

interface UserApiService {
    @FormUrlEncoded
    @POST("/user")
    suspend fun existsByUsername(@Field("username") username: String): Boolean

    @PUT("/trail/favorite/{userId}")
    suspend fun addFavoriteTrail(
        @Field("trailId") trailId: Long,
        @Path("userId") userId: Long
    ): Boolean

    @DELETE("/trail/favorite/{userId}")
    suspend fun removeFavoriteTrail(
        @Field("trailId") trailId: Long,
        @Path("userId") userId: Long
    ): Boolean
}