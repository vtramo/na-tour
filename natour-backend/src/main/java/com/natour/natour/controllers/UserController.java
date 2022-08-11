package com.natour.natour.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.natour.natour.services.user.ApplicationUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/user")
@Tag(
    name = "User Controller", 
    description = "This REST controller provides services to retrive/modify " +
        "information about NaTour users."
)
public class UserController {

    @Autowired
    private ApplicationUserService applicationUserService;
    
    @PostMapping(
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns true if the provided username already exists")
    public boolean existsByUsername(String username) {
        return applicationUserService.existsByUsername(username);
    }

    @PutMapping( 
        path = "trail/favorite/{userId}",
        consumes = {MediaType.ALL_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Adds the provided trail from the provided user's " + 
                                "list of favorite trails")
    public boolean addFavoriteTrail(
        @PathVariable("userId") long userId,
        @RequestParam long trailId
    ) {
        return applicationUserService.addFavoriteTrail(userId, trailId);
    }

    @DeleteMapping(
        path = "trail/favorite/{userId}",
        consumes = {MediaType.ALL_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Removes the provided trail from the provided user's " + 
                                "list of favorite trails")
    public boolean removeFavoriteTrail(
        @PathVariable("userId") long userId,
        @RequestParam long trailId
    ) {
        return applicationUserService.deleteFavoriteTrail(userId, trailId);
    }
}
