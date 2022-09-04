package com.example.natour.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val idChat: Long,
    val idTrail: Long,
    val usernameOwner: String,
    val usernameRecipient: String?,
    val date: String,
    val time: String,
    val message: String
)
