package com.example.natour.data.sources.network.services

import com.example.natour.data.sources.network.util.Converters
import com.example.natour.data.sources.network.LoginApiService
import com.example.natour.data.sources.network.util.URLs
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

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