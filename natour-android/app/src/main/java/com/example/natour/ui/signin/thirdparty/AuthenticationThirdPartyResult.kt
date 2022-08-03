package com.example.natour.ui.signin.thirdparty

enum class AuthenticationThirdPartyResult {
    AUTHENTICATED, NOT_AUTHENTICATED, RESET;

    operator fun not() = (this == NOT_AUTHENTICATED)
}