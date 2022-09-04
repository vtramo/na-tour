package com.natour.natour.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUnreadMessagesDto {
    private String usernameOwner;
    private int totalUnreadMessages;
}