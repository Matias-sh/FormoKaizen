package com.cocido.formokaizen.di

import com.cocido.formokaizen.data.repository.AuthRepositoryImpl
import com.cocido.formokaizen.data.repository.TarjetasRepositoryImpl
import com.cocido.formokaizen.data.repository.CategoriesRepositoryImpl
import com.cocido.formokaizen.data.repository.NotificationsRepositoryImpl
import com.cocido.formokaizen.domain.repository.AuthRepository
import com.cocido.formokaizen.domain.repository.TarjetasRepository
import com.cocido.formokaizen.domain.repository.CategoriesRepository
import com.cocido.formokaizen.domain.repository.NotificationsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
    
    @Binds
    @Singleton
    abstract fun bindTarjetasRepository(
        tarjetasRepositoryImpl: TarjetasRepositoryImpl
    ): TarjetasRepository
    
    @Binds
    @Singleton
    abstract fun bindCategoriesRepository(
        categoriesRepositoryImpl: CategoriesRepositoryImpl
    ): CategoriesRepository
    
    @Binds
    @Singleton
    abstract fun bindNotificationsRepository(
        notificationsRepositoryImpl: NotificationsRepositoryImpl
    ): NotificationsRepository
}