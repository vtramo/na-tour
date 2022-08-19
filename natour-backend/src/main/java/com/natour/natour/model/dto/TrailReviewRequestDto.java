package com.natour.natour.model.dto;

import com.natour.natour.model.Stars;

import lombok.Data;

@Data
public class TrailReviewRequestDto {
    private long idOwner;
    private long idTrail;
    private String date;
    private Stars stars;
    private String description;
}
