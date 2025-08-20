package com.cocido.formokaizen.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.cocido.formokaizen.databinding.ActivitySplashBinding
import com.cocido.formokaizen.domain.repository.AuthRepository
import com.cocido.formokaizen.presentation.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySplashBinding
    private val authViewModel: AuthViewModel by viewModels()
    
    @Inject
    lateinit var authRepository: AuthRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        splashScreen.setKeepOnScreenCondition { true }
        
        lifecycleScope.launch {
            delay(1500) // Mostrar splash por 1.5 segundos
            
            // Verificar si el usuario está logueado
            if (authRepository.isLoggedIn()) {
                // Usuario logueado, ir a MainActivity
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                // Usuario no logueado, ir a pantalla de login
                startActivity(Intent(this@SplashActivity, MainActivity::class.java).apply {
                    putExtra("show_auth", true)
                })
            }
            
            finish()
        }
    }
}