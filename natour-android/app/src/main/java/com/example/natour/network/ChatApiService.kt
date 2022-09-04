package com.example.natour.network

import com.example.natour.data.dto.ChatRequestDto
import com.example.natour.data.dto.ChatResponseDto
import com.example.natour.data.dto.ImageDto
import com.example.natour.data.dto.UpdateUnreadMessagesDto
import retrofit2.http.*

interface ChatApiService {

    @GET("/user/chat/{userId}")
    suspend fun getChats(
        @Path("userId") userId: Long
    ) : List<ChatResponseDto>

    @POST("/chat")
    suspend fun createChat(
        @Body chatDto: ChatRequestDto
    ): Boolean

    @PUT("/chat/messages/unread/{chatId}")
    suspend fun updateUnreadMessages(
        @Path("chatId") chatId: Long,
        @Body updateUnreadMessageDto: UpdateUnreadMessagesDto
    )

    @GET("/chat/image/{trailId}")
    suspend fun getTrailChatImage(@Path("trailId") trailId: Long): ImageDto

    @GET("/chat/trail/{chatId}/{trailId}")
    suspend fun setTrailToChat(
        @Path("chatId") chatId: Long,
        @Path("trailId") trailId: Long
    )
}