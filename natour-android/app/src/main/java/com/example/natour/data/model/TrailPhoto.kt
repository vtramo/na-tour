package com.example.natour.data.model

import android.graphics.drawable.Drawable

data class TrailPhoto(
    val owner: UserDetails,
    val image: Drawable,
    val position: Position
)
