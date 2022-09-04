package com.natour.natour.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessageDto {
    private long idChat;
    private String usernameOwner;
    private String usernameRecipient;
    private String date;
    private String message;
    private String time;
    private Long idTrail;
}
