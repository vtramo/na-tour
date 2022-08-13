package com.example.natour.data.util

import android.graphics.drawable.Drawable
import com.example.natour.data.model.Duration
import com.example.natour.data.model.Position
import com.example.natour.data.model.RoutePoint
import com.example.natour.data.model.TrailDifficulty
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun Long.buildRequestBody() =
    Json.encodeToString(this)
        .toRequestBody("application/json".toMediaTypeOrNull())

fun String.buildRequestBody() = toRequestBody("text/plain".toMediaTypeOrNull())

fun Drawable.buildRequestBody() =
    convertDrawableToByteArray()
        .toRequestBody("image/*".toMediaTypeOrNull())

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

fun Position.buildRequestBody() =
    Json.encodeToString(this)
        .toRequestBody("application/json".toMediaTypeOrNull())