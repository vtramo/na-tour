package com.example.natour

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes

class MainActivity : AppCompatActivity() {

    companion object {
        private lateinit var context: Application
        fun getString(@StringRes id: Int) = context.getString(id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = application
        setContentView(R.layout.activity_main)
    }
}