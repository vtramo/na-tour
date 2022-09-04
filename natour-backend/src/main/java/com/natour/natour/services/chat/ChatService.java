package com.natour.natour.services.chat;

import com.natour.natour.model.dto.ChatMessageDto;
import com.natour.natour.model.dto.ChatRequestDto;
import com.natour.natour.model.dto.ImageDto;

public interface ChatService {
    boolean create(ChatRequestDto chatDto);
    boolean sendMessage(ChatMessageDto chatMessageDto);
    void updateUnreadMessages(Long chatId, String usernameOwner, int totalUnreadMessages);
    ImageDto getTrailChatImage(long trailId);
    void setTrailToChat(long chatId, long idTrail);
}
