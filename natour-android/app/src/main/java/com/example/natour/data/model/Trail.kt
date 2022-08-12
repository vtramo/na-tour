package com.example.natour.data.model

import android.graphics.drawable.Drawable
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
    val photos: List<TrailPhoto>,
    val reviews: List<TrailReview>,
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
}
