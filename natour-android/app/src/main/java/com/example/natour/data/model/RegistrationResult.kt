package com.example.natour.data.model

enum class RegistrationResult {
    REGISTERED, NOT_REGISTERED, RESET;

    operator fun not() = (this == NOT_REGISTERED)
}