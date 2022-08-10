package com.natour.natour.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SomeSortOfTrailPhoto {
    private long idOwner;
    private long idTrail;
    private MultipartFile image;
    private SomeSortOfPosition position;
}
