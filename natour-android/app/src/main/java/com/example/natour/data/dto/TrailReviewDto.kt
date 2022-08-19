package com.example.natour.data.dto

import com.example.natour.data.model.Stars

data class TrailReviewDto(
    val idOwner: Long,
    val idTrail: Long,
    val date: String,
    val stars: Stars,
    val description: String
)
