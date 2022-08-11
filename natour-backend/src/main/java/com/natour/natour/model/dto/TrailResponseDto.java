package com.natour.natour.model.dto;

import java.util.List;

import com.natour.natour.model.Stars;
import com.natour.natour.model.TrailDifficulty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrailResponseDto {
    private long idTrail;
    private UserDetailsDto owner;
    private String name;
    private String description;
    private TrailDifficulty difficulty;
    private DurationDto duration;
    private byte[] bytesImage;
    private List<RoutePointDto> routePoints;
    private List<TrailPhotoResponseDto> photos;
    private List<TrailReviewResponseDto> reviews;
    private Stars stars;   
}
