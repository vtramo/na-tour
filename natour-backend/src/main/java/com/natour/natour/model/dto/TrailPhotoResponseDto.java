package com.natour.natour.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrailPhotoResponseDto {
    private UserDetailsDto owner;
    private byte[] bytesImage;
    private PositionDto position;
}
