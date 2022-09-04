package com.example.natour.data.dto

import com.example.natour.data.model.Chat
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

fun ChatResponseDto.toChatModel(): Chat =
    Chat(
        id,
        idTrail,
        messages,
        totalUnreadMessages,
        usernameUser1,
        usernameUser2
    )