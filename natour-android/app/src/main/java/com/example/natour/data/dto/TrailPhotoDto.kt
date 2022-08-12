package com.example.natour.data.dto

import com.example.natour.data.model.Position
import com.example.natour.data.model.UserDetails

data class TrailPhotoDto(
    val owner: UserDetails,
    val bytesImage: String,
    val position: Position
)
