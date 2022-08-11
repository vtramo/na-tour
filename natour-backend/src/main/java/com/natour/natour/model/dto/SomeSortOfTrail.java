package com.natour.natour.model.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.natour.natour.model.TrailDifficulty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SomeSortOfTrail {
    private Long idOwner;
    private String name;
    private TrailDifficulty difficulty;
    private Duration trailDuration;
    private String description;
    private List<SomeSortOfRoutePoint> routePoints;
    private MultipartFile image;
}
