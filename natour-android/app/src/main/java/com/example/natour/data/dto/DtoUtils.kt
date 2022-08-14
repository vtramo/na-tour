package com.example.natour.data.util

import com.example.natour.data.dto.TrailDto
import com.example.natour.data.dto.TrailPhotoDto
import com.example.natour.data.model.Trail
import com.example.natour.data.model.TrailPhoto
import com.example.natour.util.toDrawable

fun TrailDto.toTrailModel(): Trail =
    Trail(
        idTrail,
        owner,
        name,
        description,
        difficulty,
        duration,
        bytesImage.toDrawable(),
        routePoints,
        photos.toMutableListOfTrailPhotoModel(),
        reviews.toMutableList(),
        stars
    )

fun TrailPhotoDto.toTrailPhotoModel(): TrailPhoto =
    TrailPhoto(
        owner,
        bytesImage.toDrawable(),
        position
    )

fun List<TrailPhotoDto>.toMutableListOfTrailPhotoModel(): MutableList<TrailPhoto> =
    map { trailPhotoDto -> trailPhotoDto.toTrailPhotoModel() }
        .toMutableList()