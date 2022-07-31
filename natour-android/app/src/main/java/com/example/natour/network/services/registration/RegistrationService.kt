package com.example.natour.network.services.registration

import com.example.natour.network.Converters
import com.example.natour.network.services.URLs
import com.example.natour.network.services.login.LoginService
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

    interface RegistrationApiService {
        @FormUrlEncoded
        @POST("/registration")
        suspend fun login(
            @Field("firstName") firstName: String,
            @Field("lastName") lastName: String,
            @Field("username") username: String,
            @Field("email") email: String,
            @Field("password") password: String
        ): Boolean
    }

    val retrofitService: RegistrationApiService by lazy {
        retrofit.create(RegistrationApiService::class.java)
    }
}