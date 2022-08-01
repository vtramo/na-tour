package com.natour.natour.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.natour.natour.util.ConstantRegex;

import lombok.Data;

@Data
public class SomeSortOfUser {

    @Size(min = 1, max = 40)
    @Pattern(regexp = ConstantRegex.NAME_REGEX, message = "Invalid first name")
    private String firstName;

    @Size(min = 1, max = 40)
    @Pattern(regexp = ConstantRegex.NAME_REGEX, message = "Invalid last name")
    private String lastName;

    @Size(min = 5, max = 16)
    @Pattern(regexp = ConstantRegex.USERNAME_REGEX, message = "Invalid username")
    private String username;

    @Email(message = "Invalid email")
    private String email;

    @Size(min = 8)
    @Pattern(regexp = ConstantRegex.PASSWORD_REGEX, message = "This is not a secure password!")
    private String password;
}
