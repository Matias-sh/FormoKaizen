package com.cocido.formokaizen.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cocido.formokaizen.models.TarjetaRoja

@Dao
interface TarjetaRojaDao {
    @Insert
    suspend fun insert(tarjeta: TarjetaRoja)

    @Query("SELECT * FROM tarjetas_rojas ORDER BY id DESC")
    suspend fun getAll(): List<TarjetaRoja>
}
