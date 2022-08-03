package com.example.natour.data.sources.network

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApiService {
    @FormUrlEncoded
    @POST("/user")
    suspend fun existsByUsername(@Field("username") username: String): Boolean
}