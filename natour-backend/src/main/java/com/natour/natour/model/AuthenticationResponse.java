package com.natour.natour.model;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {

    private final boolean isAuthenticated;

    @Nullable
    @JsonUnwrapped
    private ApplicationUser user;

    @Nullable
    @JsonUnwrapped
    private Token token;

    public AuthenticationResponse(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }
}
