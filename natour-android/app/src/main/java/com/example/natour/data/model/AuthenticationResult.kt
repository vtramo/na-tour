package com.example.natour.data.model

enum class AuthenticationResult {
    AUTHENTICATED, NOT_AUTHENTICATED, RESET;

    operator fun not() = (this == NOT_AUTHENTICATED)
}