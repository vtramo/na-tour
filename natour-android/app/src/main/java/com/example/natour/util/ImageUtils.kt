package com.example.natour.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.natour.MainActivity
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.io.ByteArrayOutputStream
import java.util.*

fun Drawable.convertDrawableToByteArray(): ByteArray {
    val bitmapDrawable = this as BitmapDrawable
    val bitmap = bitmapDrawable.bitmap
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}

@SuppressLint("NewApi")
fun String.toDrawable(): Drawable =
    Base64
        .getDecoder()
        .decode(this)
        .toDrawable()

fun ByteArray.toDrawable(): Drawable =
    toBitMap()
        .toDrawable()

fun ByteArray.toBitMap(): Bitmap =
    BitmapFactory.decodeByteArray(this, 0, size)

fun Bitmap.toDrawable(): Drawable =
    BitmapDrawable(MainActivity.context.resources, this)

fun bitmapFromVector(context: Context, vectorResId: Int) : BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)!! as VectorDrawable
    val bitmap = vectorDrawable.toBitmap()
    val result = Bitmap.createScaledBitmap(
        bitmap,
        150,
        150,
        false
    )
    return BitmapDescriptorFactory.fromBitmap(result)
}