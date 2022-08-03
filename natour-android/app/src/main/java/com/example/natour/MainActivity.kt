package com.example.natour

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.natour.ui.AuthenticationViewModel
import com.example.natour.ui.signin.viewmodels.ThirdPartyLoginViewModel
import com.example.natour.ui.signin.thirdparty.FacebookLogin
import com.example.natour.ui.signin.thirdparty.GoogleLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val sharedAuthenticationViewModel: AuthenticationViewModel by viewModels()
    private lateinit var thirdPartyLoginViewModel: ThirdPartyLoginViewModel

    private lateinit var navController: NavController

    companion object {
        lateinit var context: Application

        fun getString(@StringRes id: Int) = context.getString(id)
        fun getDrawable(@DrawableRes id: Int) = AppCompatResources.getDrawable(context, id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = application
        setContentView(R.layout.activity_main)

        setupThirdPartyLoginViewModel()
        setupNavComponent()
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

    private fun setupNavComponent() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater

        val graph = inflater.inflate(R.navigation.nav_graph)
        val startDestination = determinesStartDestinationNavGraph()
        graph.setStartDestination(startDestination)

        navController = navHostFragment.navController
        navController.setGraph(graph, Bundle.EMPTY)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.loginFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun determinesStartDestinationNavGraph() : Int =
        if (sharedAuthenticationViewModel.isAlreadyLoggedIn()) {
            sharedAuthenticationViewModel.loadMainUser()
            R.id.homeFragment
        } else {
            R.id.loginFragment
        }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}