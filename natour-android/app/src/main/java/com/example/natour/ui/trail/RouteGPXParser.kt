package com.example.natour.ui.trail

import android.content.Context
import android.net.Uri
import com.example.natour.data.model.RoutePoint
import io.ticofab.androidgpxparser.parser.GPXParser
import io.ticofab.androidgpxparser.parser.domain.Gpx
import io.ticofab.androidgpxparser.parser.domain.WayPoint
import javax.inject.Inject

class RouteGPXParser @Inject constructor(
    private val gpxParser: GPXParser,
    private val context: Context
) {
    private companion object {
        const val GPX_TYPE = "application/gpx+xml"
        const val OCTET_STREAM_TYPE = "application/octet-stream"
    }

    inner class Route(private val _listOfRoutePoints: List<RoutePoint>) {
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
            if (getType(uri) != GPX_TYPE && getType(uri) != OCTET_STREAM_TYPE) {
                throw IllegalArgumentException("The type of uri must be gpx!")
            }
        }
    }

    private fun openInputStream(uri: Uri) = context.contentResolver.openInputStream(uri)!!

    private fun getListOfRoutePointsFromParsedGpx(parsedGpx: Gpx): List<RoutePoint> {
        val listOfRoutePoints = mutableListOf<RoutePoint>()
        listOfRoutePoints.addWayPointFromParsedGpx(parsedGpx) { wayPoints -> wayPoints.first() }
        listOfRoutePoints.addAll(getIntermediatePointsFromParsedGpx(parsedGpx))
        listOfRoutePoints.addWayPointFromParsedGpx(parsedGpx) { wayPoints -> wayPoints.last() }
        return listOfRoutePoints
    }

    private fun getIntermediatePointsFromParsedGpx(parsedGpx: Gpx): List<RoutePoint> =
        with(parsedGpx) {
            val listOfIntermediatePoints = mutableListOf<RoutePoint>()
            tracks.forEach { track ->
                track.trackSegments.forEach { trackSegment ->
                    trackSegment.trackPoints.forEach { trackPoint ->
                        with(trackPoint) {
                            listOfIntermediatePoints.add(RoutePoint(latitude, longitude))
                        }
                    }
                }
            }
            listOfIntermediatePoints
        }

    private fun MutableList<RoutePoint>.addWayPointFromParsedGpx(
        parsedGpx: Gpx,
        getWayPoint: (List<WayPoint>) -> (WayPoint)
    ) {
        if (parsedGpx.wayPoints.isNotEmpty()) {
            with(parsedGpx) {
                with(getWayPoint(wayPoints)) {
                    add(RoutePoint(latitude, longitude))
                }
            }
        }
    }
}