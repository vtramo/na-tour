package com.example.natour.data.util

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.example.natour.MainActivity
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