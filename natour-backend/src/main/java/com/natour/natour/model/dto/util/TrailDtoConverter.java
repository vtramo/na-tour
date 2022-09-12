package com.natour.natour.model.dto.util;

import com.natour.natour.model.dto.TrailResponseDto;
import com.natour.natour.model.entity.Trail;

public interface TrailDtoConverter {
    TrailResponseDto convertTrailEntityToTrailResponseDto(Trail trail);
}
