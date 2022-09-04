package com.natour.natour.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class ChatResponseDto {
    private long id;
    private List<ChatMessageDto> messages;
    private Integer totalUnreadMessages;
    private String usernameUser1;
    private String usernameUser2;
    private Long idTrail;

    public ChatResponseDto(
        Long id, 
        List<ChatMessageDto> messages,
        String usernameUser1,
        String usernameUser2,
        Long idTrail
    ) {
        this.id = id;
        this.messages = messages;
        this.usernameUser1 = usernameUser1;
        this.usernameUser2 = usernameUser2;
        this.idTrail = idTrail;
    }
}
