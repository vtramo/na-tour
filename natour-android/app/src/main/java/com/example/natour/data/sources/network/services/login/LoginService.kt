package com.example.natour.data.sources.network.services.login

import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.sources.network.Converters
import com.example.natour.data.sources.network.services.URLs
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

    val retrofitService: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }
}