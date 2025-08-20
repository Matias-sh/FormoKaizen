package com.cocido.formokaizen.presentation.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cocido.formokaizen.R
import com.cocido.formokaizen.databinding.ActivityMainBinding
import com.cocido.formokaizen.presentation.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val authViewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupNavigation()
        
        // Keep splash screen visible while checking auth
        splashScreen.setKeepOnScreenCondition { true }
        
        lifecycleScope.launch {
            delay(1500) // Minimum splash duration
            
            // Check authentication status
            val isLoggedIn = authViewModel.isLoggedIn()
            
            // Hide splash screen
            splashScreen.setKeepOnScreenCondition { false }
            
            // Navigate based on auth status
            if (isLoggedIn) {
                // User is logged in, stay on home
                // Navigation controller will handle this
            } else {
                // Navigate to login
                navController.navigate(R.id.splashFragment)
            }
        }
    }
    
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.tarjetasFragment,
                R.id.teamsFragment,
                R.id.notificationsFragment,
                R.id.profileFragment
            )
        )
        
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)
        
        // Ocultar bottom navigation en pantallas de auth y splash
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment, 
                R.id.loginFragment, 
                R.id.registerFragment,
                R.id.createTarjetaFragment,
                R.id.tarjetaDetailFragment -> {
                    binding.bottomNavigation.visibility = android.view.View.GONE
                    supportActionBar?.hide()
                }
                else -> {
                    binding.bottomNavigation.visibility = android.view.View.VISIBLE
                    supportActionBar?.show()
                }
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}