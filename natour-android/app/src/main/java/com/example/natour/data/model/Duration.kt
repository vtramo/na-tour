package com.example.natour.data.model

import kotlinx.serialization.Serializable

@Serializable
class Duration(
    private val months: Int,
    private val days: Int,
    private val hours: Int,
    private val minutes: Int,
) {
    init {
        if (isInvalidDuration())
            throw IllegalArgumentException("Invalid duration")
    }

    private fun isInvalidDuration() =
        areInvalidMonths()  ||
        areInvalidDays()    ||
        areInvalidHours()   ||
        areInvalidMinutes()

    private fun areInvalidMonths() = months < 0
    private fun areInvalidHours() = hours < 0 || hours > 24
    private fun areInvalidDays() = days < 0 || days > 30
    private fun areInvalidMinutes() = minutes < 0 || minutes > 60

    override fun toString(): String {
        if (months == 0 && days == 0 && hours == 0 && minutes == 0) return "0 min"
        var durationString = ""
        if (months != 0) durationString = "$months wk "
        if (days != 0) durationString += "$days d "
        if (hours != 0) durationString += "$hours hr "
        if (minutes != 0) durationString += "$minutes min"
        return durationString
    }
}