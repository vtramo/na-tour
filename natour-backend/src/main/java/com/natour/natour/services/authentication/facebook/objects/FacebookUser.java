package com.natour.natour.services.authentication.facebook.objects;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FacebookUser implements Serializable {
    @JsonProperty("id")
    private String id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String last_name;

    @JsonProperty("email")
    private String email;
}
