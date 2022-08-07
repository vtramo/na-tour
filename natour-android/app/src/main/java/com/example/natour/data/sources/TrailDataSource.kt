package com.example.natour.data.sources

import com.example.natour.R
import com.example.natour.data.model.Trail

class Datasource {

    fun loadTrails(): List<Trail>{
        return listOf<Trail>(
            Trail("Sentiero degli Dei","Giovanni Aiello", R.drawable.trail_test_image,"Italy, Europe"),
            Trail("Sentiero degli Dei","Vincenzo Tramo", R.drawable.trail_test_image,"Italy, Europe"),
            Trail("Sentiero degli Dei","Vincenzo Tramo", R.drawable.trail_test_image,"Italy, Europe"))
    }

}