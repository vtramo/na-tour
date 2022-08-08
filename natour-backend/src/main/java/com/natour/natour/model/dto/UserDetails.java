package com.natour.natour.model.dto;

import com.natour.natour.model.entity.ApplicationUser;

import lombok.Data;

@Data
public class UserDetails {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    public UserDetails(ApplicationUser appUser) {
        this.id = appUser.getId();
        this.username = appUser.getUsername();
        this.firstName = appUser.getFirstName();
        this.lastName = appUser.getLastName();
        this.email = appUser.getEmail();
    }
}
