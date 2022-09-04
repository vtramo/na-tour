package com.natour.natour.services.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natour.natour.model.dto.ChatResponseDto;
import com.natour.natour.model.dto.TrailResponseDto;
import com.natour.natour.model.dto.util.ChatDtoUtils;
import com.natour.natour.model.dto.util.TrailDtoUtils;
import com.natour.natour.model.entity.ApplicationUser;
import com.natour.natour.model.entity.Chat;
import com.natour.natour.model.entity.Trail;
import com.natour.natour.repositories.ApplicationUserRepository;
import com.natour.natour.repositories.TrailRepository;
import com.natour.natour.services.authentication.jwt.keycloak.admin.KeyCloakAdminService;
import com.natour.natour.services.user.ApplicationUserService;
import com.natour.natour.util.EntityUtils;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.java.Log;

@Log
@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {

    @Autowired
    private KeyCloakAdminService keyCloakAdminService;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private TrailRepository trailRepository;

    @Override
    public ApplicationUser save(ApplicationUser user) {
        keyCloakAdminService.saveUser(user.getUsername(), user.getPassword());
        return applicationUserRepository.save(user);
    }

    @Override
    public ApplicationUser findByUsername(String username) {
        return applicationUserRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return applicationUserRepository.existsByUsername(username);
    }

    @Override
    public boolean addFavoriteTrail(long userId, long trailId) {
        final ApplicationUser user = EntityUtils.findEntityById(
            applicationUserRepository,
            userId,
            "Invalid user ID (favorite trail owner)"
        );
        final Trail trail = EntityUtils.findEntityById(
            trailRepository,
            trailId, 
            "Invalid trail ID (favorite trail)"
        );

        final List<Trail> favoriteTrails = user.getFavoriteTrails();
        if (favoriteTrails.contains(trail)) {
            log.info("This trail already exists in " +
                        user.getUsername() + "'s favorite list");
            return true;
        }
        favoriteTrails.add(trail);

        applicationUserRepository.save(user);
        
        log.info("A new trail has been successfully added to " + 
                    user.getUsername() + "'s favorite list");
        return true;
    }

    @Override
    public boolean deleteFavoriteTrail(long userId, long trailId) {
        final ApplicationUser user = EntityUtils.findEntityById(
            applicationUserRepository,
            userId,
            "Invalid user ID (favorite trail owner)"
        );
        final Trail trail = EntityUtils.findEntityById(
            trailRepository,
            trailId, 
            "Invalid trail ID (favorite trail)"
        );

        final List<Trail> favoriteTrails = user.getFavoriteTrails();
        if (!favoriteTrails.contains(trail)) {
            log.info("This trail doesn't exist in " +
                        user.getUsername() + "'s favorite list");
            return true;
        }
        favoriteTrails.remove(trail);

        applicationUserRepository.save(user);

        log.info("A trail has been successfully deleted from " +
                    user.getUsername() + "'s favorite list. "  +
                    "Trail name: " + trail.getName());
        return true;
    }

    @Override
    public List<TrailResponseDto> getFavoriteTrails(long userId) {
        final ApplicationUser user = EntityUtils.findEntityById(
            applicationUserRepository,
            userId,
            "Invalid user ID (favorite trail list)"
        );

        List<TrailResponseDto> trails = user.getFavoriteTrails()
            .stream()
            .map(TrailDtoUtils::convertTrailEntityToTrailResponseDto)
            .collect(Collectors.toList());

        log.info("List of " + user.getUsername() + "'s favorite routes successfully" +
            "obtained: " + trails.size());

        return trails;
    }

    @Override
    public List<ChatResponseDto> getChats(long userId) {
        final ApplicationUser user = EntityUtils.findEntityById(
            applicationUserRepository,
            userId,
            "Invalid user ID (chats)"
        );

        List<ChatResponseDto> chats = user.getChats()
            .stream()
            .map(chat -> convertChatEntityToChatResponseDto(chat, user))
            .collect(Collectors.toList());

        log.info("List of " + user.getUsername() + "'s chats successfully" +
            "obtained: " + chats.size());

        return chats;
    }

    private ChatResponseDto convertChatEntityToChatResponseDto(Chat chat, ApplicationUser user) {
        ChatResponseDto chatDto = ChatDtoUtils.convertChatEntityToChatResponseDto(chat);
        int totalUnreadMessages = 
            chat.getTotUnreadMessageCounterByUsername(user.getUsername());
        chatDto.setTotalUnreadMessages(totalUnreadMessages);
        return chatDto;
    }
}
