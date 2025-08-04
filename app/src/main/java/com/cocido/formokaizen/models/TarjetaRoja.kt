package com.cocido.formokaizen.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tarjetas_rojas")
data class TarjetaRoja(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sector: String,
    val descripcion: String,
    val motivo: String,
    val destinoFinal: String,
    val fotoUri: String // Aquí guardamos la ruta/uri local de la foto
)
