package com.cocido.formokaizen.di

import android.content.Context
import androidx.room.Room
import com.cocido.formokaizen.data.local.FormoKaizenDatabase
import com.cocido.formokaizen.data.local.dao.TarjetasDao
import com.cocido.formokaizen.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideFormoKaizenDatabase(@ApplicationContext context: Context): FormoKaizenDatabase {
        return Room.databaseBuilder(
            context,
            FormoKaizenDatabase::class.java,
            FormoKaizenDatabase.DATABASE_NAME
        )
            .addMigrations(FormoKaizenDatabase.MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideUserDao(database: FormoKaizenDatabase): UserDao = database.userDao()
    
    @Provides
    fun provideTarjetasDao(database: FormoKaizenDatabase): TarjetasDao = database.tarjetasDao()
}