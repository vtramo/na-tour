package com.example.natour

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController


    companion object {
        private lateinit var context: Application
        fun getString(@StringRes id: Int) = context.getString(id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = application
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
}