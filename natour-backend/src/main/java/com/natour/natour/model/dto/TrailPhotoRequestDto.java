package com.natour.natour.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrailPhotoRequestDto {
    private long idOwner;
    private long idTrail;
    private byte[] bytesImage;
    private PositionDto position;
}
