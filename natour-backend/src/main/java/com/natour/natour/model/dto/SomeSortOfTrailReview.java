package com.natour.natour.model.dto;

import com.natour.natour.model.TrailReviewStars;

import lombok.Data;

@Data
public class SomeSortOfTrailReview {
    private long idOwner;
    private long idTrail;
    private TrailReviewStars stars;
    private String description;
}
