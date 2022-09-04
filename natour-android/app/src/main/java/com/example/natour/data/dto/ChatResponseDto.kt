package com.example.natour.data.dto

import com.example.natour.data.model.ChatMessage

data class ChatResponseDto(
    val id: Long,
    val idTrail: Long,
    val messages: List<ChatMessage>,
    val totalUnreadMessages: Int,
    val usernameUser1: String,
    val usernameUser2: String
)
