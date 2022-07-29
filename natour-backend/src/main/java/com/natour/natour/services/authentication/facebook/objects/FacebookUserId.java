package com.natour.natour.services.authentication.facebook.objects;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FacebookUserId implements Serializable {
    @JsonProperty("name")
    private final String name;

    @JsonProperty("id")
    private final String id;
}