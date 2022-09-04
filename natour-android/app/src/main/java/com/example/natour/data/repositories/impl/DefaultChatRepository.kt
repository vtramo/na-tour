package com.example.natour.data.repositories.impl

import android.graphics.drawable.Drawable
import com.example.natour.data.dto.ChatRequestDto
import com.example.natour.data.model.Chat
import com.example.natour.data.repositories.ChatRepository
import com.example.natour.data.sources.ChatDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DefaultChatRepository(
    private val chatDataSource: ChatDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ChatRepository {

    override suspend fun loadChats(mainUserId: Long): Flow<List<Chat>> = withContext(ioDispatcher) {
        chatDataSource.loadChats(mainUserId)
    }

    override suspend fun createChat(mainUserId: Long, recipientId: Long, idTrail: Long): Boolean
        = withContext(ioDispatcher) {
            chatDataSource.createChat(ChatRequestDto(mainUserId, recipientId, idTrail))
        }

    override suspend fun updateUnreadMessages(
        idChat: Long,
        usernameOwner: String,
        totalUnreadMessages: Int
    ) = withContext(ioDispatcher) {
        chatDataSource.updateUnreadMessages(idChat, usernameOwner, totalUnreadMessages)
    }

    override suspend fun getTrailChatImage(trailId: Long): Result<Drawable>
        = withContext(ioDispatcher) {
            chatDataSource.getTrailChatImage(trailId)
        }

    override suspend fun setTrailToChat(chatId: Long, idTrail: Long) = withContext(ioDispatcher) {
        chatDataSource.setTrailToChat(chatId, idTrail)
    }
}