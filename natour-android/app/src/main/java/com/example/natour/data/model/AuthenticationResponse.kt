package com.example.natour.data.model

import com.squareup.moshi.Json
import java.io.Serializable

data class AuthenticationResponse(
    val authenticated: Boolean,
    val id: Long?,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    @Json(name="access_token") val accessToken: String?,
    @Json(name="refresh_token") val refreshToken: String?
) : Serializable

