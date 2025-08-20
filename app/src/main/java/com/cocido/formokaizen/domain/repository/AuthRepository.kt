package com.cocido.formokaizen.domain.repository

import com.cocido.formokaizen.domain.entities.*
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Flow<Resource<AuthToken>>
    suspend fun register(request: RegisterRequest): Flow<Resource<AuthToken>>
    suspend fun logout(): Flow<Resource<Unit>>
    suspend fun refreshToken(): Flow<Resource<AuthToken>>
    suspend fun getCurrentUser(): Flow<Resource<User>>
    suspend fun updateProfile(user: User): Flow<Resource<User>>
    suspend fun changePassword(currentPassword: String, newPassword: String): Flow<Resource<Unit>>
    fun isLoggedIn(): Boolean
    fun getStoredToken(): String?
    suspend fun clearAuthData()
}