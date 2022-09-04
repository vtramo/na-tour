package com.example.natour.util

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDate(): String = SimpleDateFormat(
    "dd/MM/yyyy",
    Locale.getDefault()
).format(Calendar.getInstance().time)

fun getCurrentTime(): String = SimpleDateFormat(
    "h:mm a",
    Locale.getDefault()
).format(Calendar.getInstance().time)