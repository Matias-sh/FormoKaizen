package com.cocido.formokaizen.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cocido.formokaizen.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRole = intent.getStringExtra("userRole") ?: "user"
        binding.txtWelcome.text = "¡Bienvenido a FormoKaizen!\n(Rol: $userRole)"

        // Navegar a crear tarjeta roja
        binding.btnNuevaTarjeta.setOnClickListener {
            startActivity(Intent(this, NuevaTarjetaActivity::class.java))
        }

        // (Opcional) Navegar a listado de tarjetas
        binding.btnVerTarjetas.setOnClickListener {
            startActivity(Intent(this, ListadoTarjetasActivity::class.java))
        }

        // (Opcional) Botón de salir
        binding.btnSalir.setOnClickListener {
            finishAffinity() // Sale de la app
        }
    }
}
