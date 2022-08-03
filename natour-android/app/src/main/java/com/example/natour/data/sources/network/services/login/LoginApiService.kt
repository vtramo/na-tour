package com.example.natour.data.sources.network.services.login

import com.example.natour.data.model.AuthenticationResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginApiService {
    @FormUrlEncoded
    @POST("/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): AuthenticationResponse

    @FormUrlEncoded
    @POST("/login/google")
    suspend fun loginWithGoogle(
        @Field("authenticationCode") authenticationCode: String
    ): AuthenticationResponse

    @FormUrlEncoded
    @POST("/login/facebook")
    suspend fun loginWithFacebook(
        @Field("accessToken") accessToken: String
    ): AuthenticationResponse
}