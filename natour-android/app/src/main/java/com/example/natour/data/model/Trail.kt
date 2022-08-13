package com.example.natour.data.model

import android.graphics.drawable.Drawable
import android.location.Geocoder
import com.example.natour.MainActivity
import com.example.natour.R

data class Trail(
    val idTrail: Long,
    val owner: UserDetails,
    val name: String,
    val description: String,
    val difficulty: TrailDifficulty,
    val duration: Duration,
    val image: Drawable,
    val routePoints: List<RoutePoint>,
    val photos: MutableList<TrailPhoto>,
    val reviews: MutableList<TrailReview>,
    val stars: Stars
) {
    fun getStarsImage(): Drawable =
        with(MainActivity) {
            when(stars) {
                Stars.ZERO  -> getDrawable(R.drawable.zero_stars_image)!!
                Stars.ONE   -> getDrawable(R.drawable.one_stars_image)!!
                Stars.TWO   -> getDrawable(R.drawable.two_stars_image)!!
                Stars.THREE -> getDrawable(R.drawable.three_stars_image)!!
                Stars.FOUR  -> getDrawable(R.drawable.four_stars_image)!!
                Stars.FIVE  -> getDrawable(R.drawable.five_stars_image)!!
            }
        }

    fun getPositionDetails(): String {
        val geocoder = Geocoder(MainActivity.context)
        val latitude = routePoints[0].latitude
        val longitude = routePoints[0].longitude
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        return with(addresses[0]) {
            var positionResult = if (adminArea.isNullOrBlank()) "" else "$adminArea, "
            positionResult += countryName
            return@with positionResult
        }
    }
}
