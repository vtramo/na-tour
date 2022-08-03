package com.natour.natour.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.natour.natour.model.SomeSortOfUser;
import com.natour.natour.services.registration.RegistrationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/registration")
@Tag(
    name = "Registration Controller",
    description = "This REST controller provides services to register in the NaTour application"
)
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;
    
    @PostMapping(
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register the provided user in the NaTour application and " +
                         "returns true if it has succeeded, false otherwise")
    public boolean register(@Valid SomeSortOfUser user) {
        return registrationService.register(user);
    }

    @PostMapping(
        path = "/user",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns true if the provided username already exists")
    public boolean existsByUsername(String username) {
        return registrationService.existsByUsername(username);
    }
}
