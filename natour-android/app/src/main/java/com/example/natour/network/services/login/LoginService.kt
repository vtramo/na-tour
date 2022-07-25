package com.example.natour.network.services.login

import com.example.natour.network.Converters
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.*

object LoginService {
    private const val BASE_URL = "http://192.168.56.1.nip.io:8080/"

    private val okHttpClient = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Converters.createConverterFactory())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    interface LoginApiService {
        @FormUrlEncoded
        @POST("/login")
        suspend fun login(
            @Field("username") username: String,
            @Field("password") password: String
        ): Boolean
    }

    val retrofitService: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }
}