package com.natour.natour.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "CT_USERS")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
}
