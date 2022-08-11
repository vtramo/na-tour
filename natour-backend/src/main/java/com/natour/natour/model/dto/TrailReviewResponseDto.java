package com.natour.natour.model.dto;

import com.natour.natour.model.Stars;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrailReviewResponseDto {
    private UserDetailsDto owner;
    private Stars stars;
    private String description;
}
