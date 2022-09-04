package com.example.natour.data.sources.impl

import android.graphics.drawable.Drawable
import android.util.Log
import com.example.natour.data.dto.ChatRequestDto
import com.example.natour.data.dto.UpdateUnreadMessagesDto
import com.example.natour.data.model.Chat
import com.example.natour.data.sources.ChatDataSource
import com.example.natour.data.dto.toChatModel
import com.example.natour.network.ChatApiService
import com.example.natour.util.toDrawable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChatRemoteDataSource(
    private val chatApiService: ChatApiService
) : ChatDataSource {

    private companion object {
        const val TAG = "ChatRemoteDataSource"
    }

    override suspend fun loadChats(mainUserId: Long): Flow<List<Chat>> = flow {
        emit(chatApiService.getChats(mainUserId).map { it.toChatModel() })
    }

    override suspend fun createChat(chatDto: ChatRequestDto): Boolean =
        try {
            chatApiService.createChat(chatDto)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "")
            false
        }

    override suspend fun updateUnreadMessages(
        idChat: Long,
        usernameOwner: String,
        totalUnreadMessages: Int
    ) {
        try {
            chatApiService.updateUnreadMessages(
                idChat,
                UpdateUnreadMessagesDto(usernameOwner, totalUnreadMessages)
            )
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "")
        }
    }

    override suspend fun getTrailChatImage(trailId: Long): Result<Drawable> =
        try {
            val imageBytes = chatApiService.getTrailChatImage(trailId)
            Result.success(imageBytes.bytes.toDrawable())
        } catch(e: Exception) {
            Log.e(TAG, e.message ?: "")
            e.printStackTrace()
            Result.failure(e)
        }

    override suspend fun setTrailToChat(chatId: Long, idTrail: Long) {
        try {
            chatApiService.setTrailToChat(chatId, idTrail)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "")
        }
    }
}