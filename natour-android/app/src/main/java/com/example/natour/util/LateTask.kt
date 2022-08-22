package com.example.natour.util

import java.util.*

class LateTask(
    private val delayMs: Long,
    private val action: () -> (Unit)
) {
    private val timer = Timer()
    private val timerTask = object: TimerTask() {
        override fun run() = action()
    }

    fun start() = timer.schedule(timerTask, delayMs)
}