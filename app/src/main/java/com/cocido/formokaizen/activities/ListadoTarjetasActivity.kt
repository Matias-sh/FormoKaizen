package com.cocido.formokaizen.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cocido.formokaizen.adapters.TarjetaRojaAdapter
import com.cocido.formokaizen.databinding.ActivityListadoTarjetasBinding
// import com.cocido.formokaizen.db.AppDatabase
import kotlinx.coroutines.launch

class ListadoTarjetasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListadoTarjetasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListadoTarjetasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Implement listing with new database architecture
        // val db = androidx.room.Room.databaseBuilder(
        //     applicationContext,
        //     AppDatabase::class.java,
        //     "formokaizen_db"
        // ).build()

        binding.recyclerTarjetas.apply {
            layoutManager = LinearLayoutManager(this@ListadoTarjetasActivity)
            adapter = TarjetaRojaAdapter(emptyList())
        }

        // lifecycleScope.launch {
        //     val tarjetas = db.tarjetaRojaDao().getAll()
        //     runOnUiThread {
        //         binding.recyclerTarjetas.apply {
        //             layoutManager = LinearLayoutManager(this@ListadoTarjetasActivity)
        //             adapter = TarjetaRojaAdapter(tarjetas)
        //         }
        //     }
        // }
    }
}
