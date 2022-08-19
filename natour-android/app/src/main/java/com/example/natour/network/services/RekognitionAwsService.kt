package com.example.natour.network.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import aws.sdk.kotlin.services.rekognition.RekognitionClient
import aws.sdk.kotlin.services.rekognition.model.*
import aws.smithy.kotlin.runtime.auth.awscredentials.Credentials
import aws.smithy.kotlin.runtime.auth.awscredentials.CredentialsProvider
import com.example.natour.network.IllegalContentImageDetectorApiService
import java.io.File

class RekognitionAwsService : IllegalContentImageDetectorApiService {
    private companion object RekognitionCredentialsProvider : CredentialsProvider {
        const val REGION = "us-east-1"
        const val TAG = "REKOGNITION"
        override suspend fun getCredentials(): Credentials {
            return Credentials(
                "AKIAQ6F4LM54ABEHEXPE",
                "SuMn+LK4EsUvfROO4OidgZw1I5pbU6XXqLjwhkjl"
            )
        }
    }

    override suspend fun detectIllegalContent(sourceImage: String): LiveData<Boolean> {
        val isIllegalImageLiveData = MutableLiveData<Boolean>()
        val myImage = Image { this.bytes = File(sourceImage).readBytes() }

        val request = DetectModerationLabelsRequest {
            image = myImage
            minConfidence = 60f
        }

        try {
            buildRekognitionClient().use { rekClient ->
                with(rekClient.detectModerationLabels(request)) {
                    logModerationLabels(moderationLabels)
                    isIllegalImageLiveData.postValue(isIllegalImage())
                }
            }
        } catch (e: InvalidImageFormatException) {
            Log.w(TAG, "invalid image format exception")
            isIllegalImageLiveData.postValue(false)
        }

        return isIllegalImageLiveData
    }

    private fun buildRekognitionClient(): RekognitionClient = RekognitionClient {
        region = REGION
        credentialsProvider = RekognitionCredentialsProvider
    }

    private fun DetectModerationLabelsResponse.isIllegalImage() =
        moderationLabels?.isNotEmpty() ?: false

    private fun logModerationLabels(moderationLabels: List<ModerationLabel>?) {
        moderationLabels?.forEach { label ->
            Log.w(TAG,
                "Label: ${label.name} "                 +
                        "- Confidence: ${label.confidence} "   +
                        "% Parent: ${label.parentName}"
            )
        }
    }
}