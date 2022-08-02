package com.example.natour.data.sources.network.services.registration

import com.example.natour.data.sources.network.Converters
import com.example.natour.data.sources.network.services.URLs
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

object RegistrationService {

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

    val retrofitService: RegistrationApiService by lazy {
        retrofit.create(RegistrationApiService::class.java)
    }
}