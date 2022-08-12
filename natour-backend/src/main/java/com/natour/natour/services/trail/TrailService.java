package com.natour.natour.services.trail;

import java.util.List;

import com.natour.natour.model.dto.TrailRequestDto;
import com.natour.natour.model.dto.TrailPhotoRequestDto;
import com.natour.natour.model.dto.TrailReviewRequestDto;
import com.natour.natour.model.dto.TrailResponseDto;

public interface TrailService {
    public boolean saveTrail(TrailRequestDto trail);

    public boolean addReview(TrailReviewRequestDto review);

    public boolean addPhoto(TrailPhotoRequestDto someSortOfTrailPhoto);

    public List<TrailResponseDto> getTrails(int page);
}
