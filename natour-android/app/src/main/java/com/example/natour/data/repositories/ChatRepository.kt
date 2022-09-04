package com.example.natour.data.repositories

import android.graphics.drawable.Drawable
import com.example.natour.data.model.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun loadChats(mainUserId: Long): Flow<List<Chat>>
    suspend fun createChat(mainUserId: Long, recipientId: Long, idTrail: Long): Boolean
    suspend fun updateUnreadMessages(idChat: Long, usernameOwner: String, totalUnreadMessages: Int)
    suspend fun getTrailChatImage(trailId: Long): Result<Drawable>
    suspend fun setTrailToChat(chatId: Long, idTrail: Long)
}