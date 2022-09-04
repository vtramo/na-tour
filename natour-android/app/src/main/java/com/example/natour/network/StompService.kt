package com.example.natour.network

import androidx.lifecycle.LiveData
import com.example.natour.data.model.ChatMessage
import ua.naiksoftware.stomp.dto.StompMessage

interface StompService {
    val lastMessage: LiveData<StompMessage>

    fun connect(
        onConnected: () -> Unit,
        onError: (Exception) -> Unit,
        onClosed: () -> Unit,
        onFailedServerHeartBeat: () -> Unit
    )
    fun reconnect()
    fun sendMessage(chatMessage: ChatMessage, onSend: () -> Unit, onError: () -> Unit)
    fun disconnect()
}