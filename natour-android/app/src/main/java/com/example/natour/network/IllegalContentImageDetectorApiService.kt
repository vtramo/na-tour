package com.example.natour.network

import androidx.lifecycle.LiveData

interface IllegalContentImageDetectorApiService {

    suspend fun detectIllegalContent(sourceImage: String): LiveData<Boolean>
}