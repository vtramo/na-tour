package com.natour.natour.model;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("isAuthenticated")
    private final boolean isAuthenticated;

    @Nullable
    @JsonUnwrapped
    private final ApplicationUser user;

    @Nullable
    @JsonUnwrapped
    private final Token token;
}
