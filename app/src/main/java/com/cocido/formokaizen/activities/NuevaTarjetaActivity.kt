package com.cocido.formokaizen.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.cocido.formokaizen.databinding.ActivityNuevaTarjetaBinding
import com.cocido.formokaizen.db.AppDatabase
import com.cocido.formokaizen.models.TarjetaRoja
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class NuevaTarjetaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuevaTarjetaBinding
    private lateinit var db: AppDatabase
    private var fotoUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaTarjetaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "formokaizen_db").build()

        binding.btnCapturarFoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 1001)
        }

        binding.btnGuardarTarjeta.setOnClickListener {
            val sector = binding.inputSector.text.toString()
            val desc = binding.inputDescripcion.text.toString()
            val motivo = binding.inputMotivo.text.toString()
            val destino = binding.inputDestinoFinal.text.toString()
            val foto = fotoUri

            if (sector.isBlank() || desc.isBlank() || motivo.isBlank() || destino.isBlank() || foto == null) {
                Toast.makeText(this, "Todos los campos y la foto son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                db.tarjetaRojaDao().insert(
                    TarjetaRoja(
                        sector = sector,
                        descripcion = desc,
                        motivo = motivo,
                        destinoFinal = destino,
                        fotoUri = foto
                    )
                )
                runOnUiThread {
                    Toast.makeText(this@NuevaTarjetaActivity, "Tarjeta guardada", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK && data != null) {
            val bitmap = data.extras?.get("data") as? Bitmap
            bitmap?.let {
                // Guardar la imagen en almacenamiento interno
                val file = File(filesDir, "foto_tarjeta_${System.currentTimeMillis()}.jpg")
                val fos = FileOutputStream(file)
                it.compress(Bitmap.CompressFormat.JPEG, 90, fos)
                fos.close()
                fotoUri = file.absolutePath
                binding.imgFoto.setImageBitmap(it)
            }
        }
    }
}
