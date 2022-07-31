package com.example.natour.network.services.login

import com.example.natour.model.AuthenticationResponse
import com.example.natour.network.Converters
import com.example.natour.network.services.URLs
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.*

object LoginService {

    private val okHttpClient = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Converters.createConverterFactory())
        .baseUrl(URLs.BACKEND)
        .client(okHttpClient)
        .build()

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

    val retrofitService: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }
}