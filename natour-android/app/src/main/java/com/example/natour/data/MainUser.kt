package com.example.natour.data

import com.example.natour.data.model.AuthenticatedUser
import com.example.natour.data.model.AuthenticationResponse
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

    fun set(authenticatedUser: AuthenticatedUser) {
        with(details) {
            id          = authenticatedUser.id
            username    = authenticatedUser.username
            firstName   = authenticatedUser.firstName
            lastName    = authenticatedUser.lastName
            email       = authenticatedUser.email
        }

        with(token) {
            accessToken = authenticatedUser.accessToken
            refreshToken = authenticatedUser.refreshToken
        }
    }

    fun clear() {
        details = Details()
        token   = Token()
    }

    override fun toString() = "$details"
}
