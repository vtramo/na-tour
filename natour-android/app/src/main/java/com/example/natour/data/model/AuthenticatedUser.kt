package com.example.natour.data.model

import java.io.Serializable

data class AuthenticatedUser(
    val id: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String
) : Serializable {
    constructor(authentication: AuthenticationResponse) : this(
        authentication.id!!,
        authentication.username!!,
        authentication.firstName!!,
        authentication.lastName!!,
        authentication.email!!,
        authentication.accessToken!!,
        authentication.refreshToken!!
    )
}
