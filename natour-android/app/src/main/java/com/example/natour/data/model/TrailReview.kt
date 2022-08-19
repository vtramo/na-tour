package com.example.natour.data.model

import android.graphics.drawable.Drawable
import com.example.natour.MainActivity
import com.example.natour.R

data class TrailReview(
    val owner: UserDetails,
    val stars: Stars,
    val description: String,
    val date: String
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
