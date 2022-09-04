package com.natour.natour.model.dto.util;

import com.natour.natour.model.dto.ChatMessageDto;
import com.natour.natour.model.dto.ChatResponseDto;
import com.natour.natour.model.entity.Chat;
import com.natour.natour.model.entity.ChatMessage;

import java.util.List;
import static java.util.stream.Collectors.*;

public abstract class ChatDtoUtils {

    public static ChatResponseDto convertChatEntityToChatResponseDto(Chat chat) {
        return new ChatResponseDto(
            chat.getId(),
            convertListOfChatMessageEntityToListOfChatMessageDto(chat.getMessages()),
            chat.getUsernameUser1(),
            chat.getUsernameUser2(),
            chat.getIdTrail()
        );
    }

    public static List<ChatMessageDto> 
        convertListOfChatMessageEntityToListOfChatMessageDto(
            List<ChatMessage> messages
    ) {
        return messages.stream()
            .map(ChatDtoUtils::convertChatMessageEntityToChatMessageDto)
            .collect(toList());
    }

    public static ChatMessageDto convertChatMessageEntityToChatMessageDto(
        ChatMessage chatMessage
    ) {
        return new ChatMessageDto(
            chatMessage.getChat().getId(), 
            chatMessage.getUsernameOwner(), 
            null, 
            chatMessage.getDate(), 
            chatMessage.getMessage(),
            chatMessage.getTime(),
            chatMessage.getIdTrail()
        );
    }
    
}
