package com.example.natour.data.repositories.util

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import com.example.natour.data.model.Duration
import com.example.natour.data.model.RoutePoint
import com.example.natour.data.model.TrailDifficulty
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

object RequestBodyUtil {
    fun Long.buildRequestBody() =
        Json.encodeToString(this)
            .toRequestBody("application/json".toMediaTypeOrNull())

    fun String.buildRequestBody() = toRequestBody("text/plain".toMediaTypeOrNull())

    fun Drawable.buildRequestBody() =
        convertDrawableToByteArray(this)
            .toRequestBody("image/*".toMediaTypeOrNull())

    private fun convertDrawableToByteArray(drawable: Drawable): ByteArray {
        val bitmapDrawable = drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    fun Duration.buildRequestBody() =
        Json.encodeToString(this)
            .toRequestBody("application/json".toMediaTypeOrNull())

    fun buildImageMultipartBody(imageRequestBody: RequestBody) =
        MultipartBody.Part.createFormData("image", "image", imageRequestBody)

    fun TrailDifficulty.buildRequestBody() =
        Json.encodeToString(this)
            .toRequestBody("application/json".toMediaTypeOrNull())

    fun List<RoutePoint>.buildRequestBody() =
        Json.encodeToString(value = this)
            .toRequestBody("application/json".toMediaTypeOrNull())
}