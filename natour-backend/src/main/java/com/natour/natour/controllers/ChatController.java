package com.natour.natour.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.natour.natour.model.dto.ChatMessageDto;
import com.natour.natour.model.dto.ChatRequestDto;
import com.natour.natour.model.dto.ImageDto;
import com.natour.natour.model.dto.UpdateUnreadMessagesDto;
import com.natour.natour.services.chat.ChatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/chat")
@Tag(
    name = "Chat Controller", 
    description = "This REST controller provides services to create/modify chats " +
        "between users or to send messages to another user"
)
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat")
    @Operation(summary = "Save and send a message to the specified username.")
    public void send(
        SimpMessageHeaderAccessor sha, 
        @Payload ChatMessageDto chatMessageDto
    ) {
        chatService.sendMessage(chatMessageDto);
    }

    @PostMapping(
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a chat between two users.")
    public boolean createChat(@RequestBody ChatRequestDto chatDto) {
        return chatService.create(chatDto);
    }

    @PutMapping(
        path="/messages/unread/{chatId}",
        consumes = {MediaType.ALL_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Updates the unread messages of the specified user" +
        " in the specified chat.")
    public void updateUnreadMessages(
        @PathVariable Long chatId, 
        @RequestBody UpdateUnreadMessagesDto updateUnreadMessagesDto
    ) {
        chatService.updateUnreadMessages(
            chatId, 
            updateUnreadMessagesDto.getUsernameOwner(), 
            updateUnreadMessagesDto.getTotalUnreadMessages()
        );
    }

    @GetMapping(
        path = "/image/{trailId}",
        consumes = {MediaType.ALL_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns the main image of the specified trail.")
    public ImageDto getTrailChatImage(@PathVariable long trailId) {
        return chatService.getTrailChatImage(trailId);
    }

    @GetMapping(
        path = "/trail/{chatId}/{trailId}",
        consumes = {MediaType.ALL_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Associate the specified trail with the specified chat.")
    public void setTrailToChat(
        @PathVariable long chatId,
        @PathVariable long trailId    
    ) {
        chatService.setTrailToChat(chatId, trailId);
    }
}

