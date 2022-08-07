package com.example.natour.ui.route.util

import android.content.Context
import android.net.Uri
import io.ticofab.androidgpxparser.parser.GPXParser
import io.ticofab.androidgpxparser.parser.domain.Gpx
import javax.inject.Inject

class RouteGPXParser @Inject constructor(
    private val gpxParser: GPXParser,
    private val context: Context
) {
    private companion object {
        const val GPX_TYPE = "application/gpx+xml"
    }

    inner class Route(private val _listOfRoutePoints: List<Pair<Double, Double>>) {
        val listOfRoutePoints
            get() = _listOfRoutePoints
    }

    fun parse(gpxFileUri: Uri): Route {
        checkTypeUri(gpxFileUri)
        val inputStream = openInputStream(gpxFileUri)
        val parsedGpx = gpxParser.parse(inputStream)
        val listOfRoutePoints = getListOfRoutePointsFromParsedGpx(parsedGpx)
        return Route(listOfRoutePoints)
    }

    private fun checkTypeUri(uri: Uri) {
        with(context.contentResolver) {
            if (getType(uri) != GPX_TYPE) {
                throw IllegalArgumentException("The type of uri must be gpx!")
            }
        }
    }

    private fun openInputStream(uri: Uri) = context.contentResolver.openInputStream(uri)!!

    private fun getListOfRoutePointsFromParsedGpx(parsedGpx: Gpx): List<Pair<Double, Double>> {
        val listOfRoutePoints = mutableListOf<Pair<Double, Double>>()
        listOfRoutePoints.add(getStartingPointFromParsedGpx(parsedGpx))
        listOfRoutePoints.addAll(getIntermediatePointsFromParsedGpx(parsedGpx))
        listOfRoutePoints.add(getDestinationPointFromParsedGpx(parsedGpx))
        return listOfRoutePoints
    }

    private fun getStartingPointFromParsedGpx(parsedGpx: Gpx): Pair<Double, Double> =
        with(parsedGpx) {
            with(wayPoints.first()) {
                Pair(latitude, longitude)
            }
        }

    private fun getIntermediatePointsFromParsedGpx(parsedGpx: Gpx): List<Pair<Double, Double>> =
        with(parsedGpx) {
            val listOfIntermediatePoints = mutableListOf<Pair<Double, Double>>()
            tracks.forEach { track ->
                track.trackSegments.forEach { trackSegment ->
                    trackSegment.trackPoints.forEach { trackPoint ->
                        with(trackPoint) {
                            listOfIntermediatePoints.add(Pair(latitude, longitude))
                        }
                    }
                }
            }
            listOfIntermediatePoints
        }

    private fun getDestinationPointFromParsedGpx(parsedGpx: Gpx): Pair<Double, Double> =
        with(parsedGpx) {
            with(wayPoints.last()) {
                Pair(latitude, longitude)
            }
        }
}