package com.natour.natour.services.trail;

import com.natour.natour.model.dto.SomeSortOfTrail;
import com.natour.natour.model.dto.SomeSortOfTrailPhoto;
import com.natour.natour.model.dto.SomeSortOfTrailReview;

public interface TrailService {
    public boolean saveTrail(SomeSortOfTrail trail);

    public byte[] getTrail(Long id);

    public boolean addReview(SomeSortOfTrailReview review);

    public boolean addPhoto(SomeSortOfTrailPhoto someSortOfTrailPhoto);
}
