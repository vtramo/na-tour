package com.natour.natour.model.dto;

import java.util.List;

import com.natour.natour.model.TrailDifficulty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrailRequestDto {
    private Long idOwner;
    private String name;
    private TrailDifficulty difficulty;
    private DurationDto trailDuration;
    private String description;
    private List<RoutePointDto> routePoints;
    private byte[] bytesImage;
}
