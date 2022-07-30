package com.example.natour.signup.registration

import android.util.Patterns

object Regex {
    val NAME_REGEX = "^(?=.{1,40}\$)[a-zA-Z]+(?:[-'\\s][a-zA-Z]+)*\$".toRegex()
    val EMAIL_REGEX = Patterns.EMAIL_ADDRESS.toRegex()
}