package com.example.natour.data.model

data class Chat(
    val id: Long,
    var idTrail: Long,
    var messages: List<ChatMessage>,
    var totalUnreadMessages: Int,
    val usernameUser1: String,
    val usernameUser2: String
)
