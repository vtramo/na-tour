package com.example.natour.user

import com.example.natour.model.AuthenticationResponse
import java.lang.IllegalArgumentException

object MainUser {
    private data class Details(
        var id:         Long   = Long.MIN_VALUE,
        var username:   String = "",
        var firstName:  String = "",
        var lastName:   String = "",
        var email:      String = ""
    )

    private data class Token(
        var accessToken:    String = "",
        var refreshToken:   String = ""
    )

    private var details = Details()
    val id          get() = details.id
    val username    get() = details.username
    val firstName   get() = details.firstName
    val lastName    get() = details.lastName
    val email       get() = details.email

    private var token = Token()
    val accessToken     get() = token.accessToken
    val refreshToken    get() = token.refreshToken

    fun set(authentication: AuthenticationResponse) {
        if (!authentication.authenticated)
            throw IllegalArgumentException("Authentication object must be authenticated!")

        with(details) {
            id          = authentication.id!!
            username    = authentication.username!!
            firstName   = authentication.firstName!!
            lastName    = authentication.lastName!!
            email       = authentication.email!!
        }

        with(token) {
            accessToken     = authentication.accessToken!!
            refreshToken    = authentication.refreshToken!!
        }
    }

    fun clear() {
        details = Details()
        token   = Token()
    }
}
