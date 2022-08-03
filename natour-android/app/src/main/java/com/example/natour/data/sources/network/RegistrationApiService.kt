package com.example.natour.data.sources.network

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegistrationApiService {
    @FormUrlEncoded
    @POST("/registration")
    suspend fun register(
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Boolean
}