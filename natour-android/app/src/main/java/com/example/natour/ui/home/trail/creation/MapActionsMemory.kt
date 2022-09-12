package com.example.natour.ui.home.trail.creation

import com.example.natour.ui.home.trail.creation.MapActionsMemory.MapActionType.*

class MapActionsMemory {

    enum class MapActionType {
        UNDO, DELETE;
    }

    class MapAction(
        private val _mapActionType: MapActionType,
        private val _routePoints: List<Pair<Double, Double>>,
        private val _isFirstPoint: Boolean
    ) {
        val type get() = _mapActionType
        val routePoints get() = _routePoints
        val isFirstPoint get() = _isFirstPoint

        override fun equals(other: Any?): Boolean {
            return if (other is MapAction) {
                other.type == this.type &&
                        other.routePoints == this.routePoints &&
                        other.isFirstPoint == this.isFirstPoint
            } else false
        }

        override fun hashCode(): Int {
            var result = _mapActionType.hashCode()
            result = 31 * result + _routePoints.hashCode()
            result = 31 * result + _isFirstPoint.hashCode()
            return result
        }
    }

    private var actions = mutableListOf<MapAction>()

    fun addAction(
        type: MapActionType,
        routePoints: List<Pair<Double, Double>>,
        isFirstPoint: Boolean = false
    ): Boolean {
        checkParameters(type, routePoints, isFirstPoint)
        return actions.add(MapAction(type, routePoints, isFirstPoint))
    }

    private fun checkParameters(
        type: MapActionType,
        routePoints: List<Pair<Double, Double>>,
        isFirstPoint: Boolean
    ) {
        if (routePoints.isEmpty())
            throw IllegalArgumentException("The list of route points can't be empty!")

        if (isFirstPoint && routePoints.size != 1)
            throw IllegalArgumentException(
                "The list of route points must contain only one point if the " +
                        "'isFirstPoint' flag is equal to true!"
            )

        if (isFirstPoint && type == DELETE)
            throw IllegalArgumentException(
                "The action type can't be 'DELETE' if the 'isFirstPoint' flag is equal to true!"
            )

    }

    fun isEmpty() = actions.isEmpty()
    fun getLastAction() = actions.removeLast()
    fun reset() {
        actions = mutableListOf()
    }
}