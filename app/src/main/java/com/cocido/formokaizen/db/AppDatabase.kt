package com.cocido.formokaizen.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cocido.formokaizen.models.TarjetaRoja
import com.cocido.formokaizen.models.User

@Database(entities = [User::class, TarjetaRoja::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tarjetaRojaDao(): TarjetaRojaDao
}

