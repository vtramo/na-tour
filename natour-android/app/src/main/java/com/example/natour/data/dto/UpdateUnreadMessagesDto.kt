package com.example.natour.data.dto

data class UpdateUnreadMessagesDto(
    val usernameOwner: String,
    val totalUnreadMessages: Int
)
