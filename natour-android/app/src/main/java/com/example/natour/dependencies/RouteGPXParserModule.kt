package com.example.natour.dependencies

import android.content.Context
import com.example.natour.ui.trail.util.RouteGPXParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ticofab.androidgpxparser.parser.GPXParser
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RouteGPXParserModule {

    @Provides
    @Singleton
    fun provideGpxParser(): GPXParser = GPXParser()

    @Provides
    @Singleton
    fun provideRouteGpxParser(gpxParser: GPXParser, context: Context): RouteGPXParser =
        RouteGPXParser(gpxParser, context)
}