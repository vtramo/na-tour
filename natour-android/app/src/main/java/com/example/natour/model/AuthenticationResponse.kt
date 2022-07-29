package com.example.natour.model

import com.squareup.moshi.Json

data class AuthenticationResponse(
    val authenticated: Boolean,
    val id: Long?,
    val username: String?,
    val email: String?,
    @Json(name="access_token") val accessToken: String?,
    @Json(name="refresh_token") val refreshToken: String?
)

