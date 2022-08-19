package com.natour.natour.services.user;

import java.util.List;

import com.natour.natour.model.dto.TrailResponseDto;
import com.natour.natour.model.entity.ApplicationUser;

public interface ApplicationUserService {
    ApplicationUser save(ApplicationUser user);
    ApplicationUser findByUsername(String username);
    boolean existsByUsername(String username);
    boolean addFavoriteTrail(long userId, long trailId);
    boolean deleteFavoriteTrail(long userId, long trailId);
    List<TrailResponseDto> getFavoriteTrails(long userId);
}
