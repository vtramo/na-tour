package com.natour.natour.model.dto;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.natour.natour.model.Token;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {

    private final boolean isAuthenticated;

    @Nullable
    @JsonUnwrapped
    private UserDetails user;

    @Nullable
    @JsonUnwrapped
    private Token token;

    public AuthenticationResponse(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }
}
