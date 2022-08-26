package com.example.natour.data.sources.impl

fun buildAuthHeader(accessToken: String) = "Bearer $accessToken"