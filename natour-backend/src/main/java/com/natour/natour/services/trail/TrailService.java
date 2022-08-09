package com.natour.natour.services.trail;

import com.natour.natour.model.dto.SomeSortOfTrail;

public interface TrailService {
    public boolean saveTrail(SomeSortOfTrail trail);

    public byte[] getTrail(Long id);
}
