package com.example.natour.ui.home.trail.creation

import com.example.natour.ui.home.trail.creation.MapActionsMemory.MapActionType.*

class MapActionsMemory() {

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
        val isFirstPoint get() = _isFirstPoint && type != DELETE
    }

    private var actions = mutableListOf<MapAction>()

    fun addAction(
        type: MapActionType,
        routePoints: List<Pair<Double, Double>>,
        isFirstPoint: Boolean = false
    ) {
        actions.add(MapAction(type, routePoints, isFirstPoint))
    }

    fun isEmpty() = actions.isEmpty()

    fun getLastAction() = actions.removeLast()

    fun reset() {
        actions = mutableListOf()
    }
}