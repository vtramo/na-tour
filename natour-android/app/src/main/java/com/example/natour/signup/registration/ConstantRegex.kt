package com.example.natour.signup.registration

import android.util.Patterns

object ConstantRegex {
    val NAME_REGEX = "^(?=.{1,40}\$)[a-zA-Z]+(?:[-'\\s][a-zA-Z]+)*\$".toRegex()
    val EMAIL_REGEX = Patterns.EMAIL_ADDRESS.toRegex()
    val USERNAME_REGEX = "^(?=.{8,16}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])\$".toRegex()
}