package com.example.natour

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.natour.ui.MainUserViewModel
import com.example.natour.ui.signin.ThirdPartyLoginViewModel
import com.example.natour.ui.signin.FacebookLogin
import com.example.natour.ui.signin.GoogleLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainUserViewModel: MainUserViewModel by viewModels()
    private lateinit var thirdPartyLoginViewModel: ThirdPartyLoginViewModel

    private lateinit var navController: NavController

    companion object {
        lateinit var context: Application

        fun getString(@StringRes id: Int) = context.getString(id)
        fun getDrawable(@DrawableRes id: Int) = AppCompatResources.getDrawable(context, id)
        @RequiresApi(Build.VERSION_CODES.M)
        fun getColor(@ColorRes id: Int) = ContextCompat.getColor(context, id)
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = application
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        loadMainUser()
        setupThirdPartyLoginViewModel()

        window.statusBarColor = context.getColor(R.color.gray)
    }

    private fun loadMainUser() {
        mainUserViewModel.mainUser.observe(this) { mainUser ->
            setupNavComponent(
                startDestination = if (mainUser != null) R.id.home_nav_graph else R.id.loginFragment
            )
            mainUserViewModel.mainUser.removeObservers(this)
        }
        mainUserViewModel.loadMainUser()
    }

    private fun setupThirdPartyLoginViewModel() {
        thirdPartyLoginViewModel = ViewModelProvider(
            this,
            ThirdPartyLoginViewModel.ThirdPartyLoginViewModelFactory(
                GoogleLogin(this),
                FacebookLogin(this)
            )
        )[ThirdPartyLoginViewModel::class.java]
    }

    private fun setupNavComponent(startDestination: Int) {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater

        val graph = inflater.inflate(R.navigation.nav_graph)
        graph.setStartDestination(startDestination)

        navController = navHostFragment.navController
        navController.setGraph(graph, Bundle.EMPTY)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.loginFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}