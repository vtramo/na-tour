package com.example.natour.data.model

enum class Stars {
    ZERO, ONE, TWO, THREE, FOUR, FIVE;

    companion object {
        fun intToEnumValue(int: Int): Stars = when(int) {
            0 -> ZERO
            1 -> ONE
            2 -> TWO
            3 -> THREE
            4 -> FOUR
            5 -> FIVE
            else -> throw IllegalArgumentException("Unknown number stars enum value")
        }
    }
}