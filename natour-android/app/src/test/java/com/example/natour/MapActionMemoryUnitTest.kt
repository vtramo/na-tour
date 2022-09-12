package com.example.natour

import com.example.natour.ui.home.trail.creation.MapActionsMemory
import com.example.natour.ui.home.trail.creation.MapActionsMemory.*
import org.junit.Assert.*
import org.junit.jupiter.api.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("A MapActionMemory")
class MapActionMemoryUnitTest {

    @DisplayName("When adding an action")
    class AddActionMethodTest {

        private val mMapActionMemory = MapActionsMemory()

        @AfterEach
        private fun resetMapActionMemory() = mMapActionMemory.reset()

        @Test
        @DisplayName("should throw IllegalArgumentException when passing an empty route points list")
        fun shouldThrowIAEWhenPassingAnEmptyRoutePointsList() {
            val mapActionType = MapActionType.DELETE
            val routePoints = listOf<Pair<Double, Double>>()
            val isFirstPoint = false


            assertThrows(IllegalArgumentException::class.java) {
                mMapActionMemory.addAction(mapActionType, routePoints, isFirstPoint)
            }
        }

        @Test
        @DisplayName("should throwIllegalArgumentException when passing a list with more than " +
                "one points and 'isFirstPoint' flag equal to true")
        fun shouldThrowIAEWhenPassingAListWithMoreThanOnePointsAndIsFirstPointFlagEqualToTrue(){
            val mapActionType = MapActionType.UNDO
            val routePoints = listOf(Pair(2.0, 3.0), Pair(2.0, 2.0))
            val isFirstPoint = true


            assertThrows(IllegalArgumentException::class.java) {
                mMapActionMemory.addAction(mapActionType, routePoints, isFirstPoint)
            }
        }

        @Test
        @DisplayName("should throwIllegalArgumentException when passing a delete action and " +
                "'isFirstPoint' flag equal to true")
        fun shouldThrowIAEWhenPassingADeleteActionAndIsFirstPointFlagEqualToTrue() {
            val mapActionType = MapActionType.DELETE
            val routePoints = listOf(Pair(2.0, 3.0), Pair(2.0, 2.0))
            val isFirstPoint = true

            assertThrows(IllegalArgumentException::class.java) {
                mMapActionMemory.addAction(mapActionType, routePoints, isFirstPoint)
            }
        }

        @Test
        @DisplayName("should add correctly when passing a delete action with one point " +
                "and 'isFirstPoint' flag equal to false")
        fun shouldAddCorrectlyWhenPassingADeleteActionWithOnePointAndIsFirstPointFlagEqualToFalse() {
            val mapActionType = MapActionType.DELETE
            val routePoints = listOf(Pair(2.0, 3.0))
            val isFirstPoint = false
            val mapActionExcepted = MapAction(mapActionType, routePoints, isFirstPoint)

            val hasBeenSuccessfullyAdded = mMapActionMemory.addAction(
                mapActionType,
                routePoints,
                isFirstPoint
            )

            assertAll(listOf {
                assertEquals(hasBeenSuccessfullyAdded, true)
                assertEquals(mapActionExcepted, mMapActionMemory.getLastAction())
            })
        }

        @Test
        @DisplayName("should add correctly when passing a delete action with two points " +
                "and 'isFirstPoint' flag equal to false")
        fun shouldAddCorrectlyWhenPassingADeleteActionWithTwoPointsAndIsFirstPointFlagEqualToFalse() {
            val mapActionType = MapActionType.DELETE
            val routePoints = listOf(Pair(2.0, 3.0), Pair(2.0, 2.0))
            val isFirstPoint = false
            val mapActionExcepted = MapAction(mapActionType, routePoints, isFirstPoint)

            val hasBeenSuccessfullyAdded = mMapActionMemory.addAction(
                mapActionType,
                routePoints,
                isFirstPoint
            )

            assertAll(listOf {
                assertEquals(hasBeenSuccessfullyAdded, true)
                assertEquals(mapActionExcepted, mMapActionMemory.getLastAction())
            })
        }

        @Test
        @DisplayName("should add correctly when passing an undo action with one point " +
                "and 'isFirstPoint' flag equal to true")
        fun shouldAddCorrectlyWhenPassingAUndoActionWithOnePointsAndIsFirstPointFlagEqualToTrue() {
            val mapActionType = MapActionType.UNDO
            val routePoints = listOf(Pair(2.0, 3.0))
            val isFirstPoint = true
            val mapActionExcepted = MapAction(mapActionType, routePoints, isFirstPoint)

            val hasBeenSuccessfullyAdded = mMapActionMemory.addAction(
                mapActionType,
                routePoints,
                isFirstPoint
            )

            assertAll(listOf {
                assertEquals(hasBeenSuccessfullyAdded, true)
                assertEquals(mapActionExcepted, mMapActionMemory.getLastAction())
            })
        }

        @Test
        @DisplayName("should add correctly when passing an undo action with two points " +
                "and 'isFirstPoint' flag equal to false")
        fun shouldAddCorrectlyWhenPassingAUndoActionWithTwoPointsAndIsFirstPointFlagEqualToFalse() {
            val mapActionType = MapActionType.UNDO
            val routePoints = listOf(Pair(2.0, 3.0), Pair(3.0, 3.0))
            val isFirstPoint = false
            val mapActionExcepted = MapAction(mapActionType, routePoints, isFirstPoint)

            val hasBeenSuccessfullyAdded = mMapActionMemory.addAction(
                mapActionType,
                routePoints,
                isFirstPoint
            )

            assertAll(listOf {
                assertEquals(hasBeenSuccessfullyAdded, true)
                assertEquals(mapActionExcepted, mMapActionMemory.getLastAction())
            })
        }

    }
}