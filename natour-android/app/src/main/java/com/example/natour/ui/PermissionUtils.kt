package com.example.natour.ui

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

/**
 * Utility class for access to runtime permissions.
 */
object PermissionUtils {

    const val LOCATION_PERMISSION_REQUEST_CODE = 1

    /**
     * Requests the fine and coarse location permissions.
     */
    fun requestLocationPermissions(
        activity: AppCompatActivity,
        requestId: Int,
    ) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            requestId
        )
    }
}