package com.example.natour.data.sources

import android.graphics.drawable.Drawable
import com.example.natour.data.dto.ChatRequestDto
import com.example.natour.data.model.Chat
import kotlinx.coroutines.flow.Flow

interface ChatDataSource {

    suspend fun loadChats(mainUserId: Long): Flow<List<Chat>>
    suspend fun createChat(chatDto: ChatRequestDto): Boolean
    suspend fun updateUnreadMessages(idChat: Long, usernameOwner: String, totalUnreadMessages: Int)
    suspend fun getTrailChatImage(trailId: Long): Result<Drawable>
    suspend fun setTrailToChat(chatId: Long, idTrail: Long)
}