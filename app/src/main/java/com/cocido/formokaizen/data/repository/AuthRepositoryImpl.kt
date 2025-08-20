package com.cocido.formokaizen.data.repository

import com.cocido.formokaizen.data.local.dao.UserDao
import com.cocido.formokaizen.data.local.entities.toEntity
import com.cocido.formokaizen.data.local.entities.toDomainModel
import com.cocido.formokaizen.data.remote.api.AuthApi
import com.cocido.formokaizen.data.remote.dto.*
import com.cocido.formokaizen.domain.entities.*
import com.cocido.formokaizen.domain.repository.AuthRepository
import com.cocido.formokaizen.utils.Resource
import com.cocido.formokaizen.utils.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userDao: UserDao,
    private val tokenManager: TokenManager
) : AuthRepository {
    
    override suspend fun login(email: String, password: String): Flow<Resource<AuthToken>> = flow {
        emit(Resource.Loading())
        
        try {
            val response = authApi.login(com.cocido.formokaizen.data.remote.dto.LoginRequest(email, password))
            
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                
                // Save tokens
                tokenManager.saveTokens(authResponse.access, authResponse.refresh)
                tokenManager.saveUserInfo(
                    authResponse.user.id.toString(),
                    authResponse.user.email,
                    authResponse.user.role
                )
                
                // Save user to local DB
                userDao.insertUser(authResponse.user.toDomainModel().toEntity())
                
                val authToken = AuthToken(
                    accessToken = authResponse.access,
                    refreshToken = authResponse.refresh,
                    user = authResponse.user.toDomainModel()
                )
                
                emit(Resource.Success(authToken))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error de autenticación"
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error de conexión"))
        }
    }
    
    override suspend fun register(request: com.cocido.formokaizen.domain.entities.RegisterRequest): Flow<Resource<AuthToken>> = flow {
        emit(Resource.Loading())
        
        try {
            val registerDto = com.cocido.formokaizen.data.remote.dto.RegisterRequest(
                username = request.username,
                email = request.email,
                password = request.password,
                passwordConfirm = request.passwordConfirm,
                firstName = request.firstName,
                lastName = request.lastName,
                employeeId = request.employeeId,
                phone = request.phone,
                department = request.department,
                position = request.position
            )
            
            val response = authApi.register(registerDto)
            
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                
                // Save tokens
                tokenManager.saveTokens(authResponse.access, authResponse.refresh)
                tokenManager.saveUserInfo(
                    authResponse.user.id.toString(),
                    authResponse.user.email,
                    authResponse.user.role
                )
                
                // Save user to local DB
                userDao.insertUser(authResponse.user.toDomainModel().toEntity())
                
                val authToken = AuthToken(
                    accessToken = authResponse.access,
                    refreshToken = authResponse.refresh,
                    user = authResponse.user.toDomainModel()
                )
                
                emit(Resource.Success(authToken))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error en el registro"
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error de conexión"))
        }
    }
    
    override suspend fun logout(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            // Clear tokens and local data
            tokenManager.clearTokens()
            userDao.clearUsers()
            
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error al cerrar sesión"))
        }
    }
    
    override suspend fun refreshToken(): Flow<Resource<AuthToken>> = flow {
        emit(Resource.Loading())
        
        try {
            val refreshToken = tokenManager.getRefreshToken()
            if (refreshToken != null) {
                val response = authApi.refreshToken(RefreshTokenRequest(refreshToken))
                
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    
                    // Save new tokens
                    tokenManager.saveTokens(authResponse.access, authResponse.refresh)
                    
                    val authToken = AuthToken(
                        accessToken = authResponse.access,
                        refreshToken = authResponse.refresh,
                        user = authResponse.user.toDomainModel()
                    )
                    
                    emit(Resource.Success(authToken))
                } else {
                    emit(Resource.Error("No se pudo renovar el token"))
                }
            } else {
                emit(Resource.Error("No hay token de actualización"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error al renovar token"))
        }
    }
    
    override suspend fun getCurrentUser(): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        
        try {
            // First try to get from local DB
            val userId = tokenManager.getUserId()?.toIntOrNull()
            if (userId != null) {
                val localUser = userDao.getUserById(userId)
                if (localUser != null) {
                    emit(Resource.Success(localUser.toDomainModel()))
                    return@flow
                }
            }
            
            // If not in local DB, fetch from API
            val response = authApi.getCurrentUser()
            
            if (response.isSuccessful && response.body() != null) {
                val userDto = response.body()!!
                val user = userDto.toDomainModel()
                
                // Save to local DB
                userDao.insertUser(user.toEntity())
                
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error("No se pudo obtener información del usuario"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error al obtener usuario"))
        }
    }
    
    override suspend fun updateProfile(user: User): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        
        try {
            val response = authApi.updateProfile(user.toDto())
            
            if (response.isSuccessful && response.body() != null) {
                val updatedUser = response.body()!!.user?.toDomainModel() ?: user
                
                // Update local DB
                userDao.updateUser(updatedUser.toEntity())
                
                emit(Resource.Success(updatedUser))
            } else {
                emit(Resource.Error("No se pudo actualizar el perfil"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error al actualizar perfil"))
        }
    }
    
    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            val request = ChangePasswordRequest(
                currentPassword = currentPassword,
                newPassword = newPassword,
                newPasswordConfirm = newPassword
            )
            
            val response = authApi.changePassword(request)
            
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("No se pudo cambiar la contraseña"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error al cambiar contraseña"))
        }
    }
    
    override fun isLoggedIn(): Boolean {
        return try {
            kotlinx.coroutines.runBlocking { tokenManager.isLoggedIn() }
        } catch (e: Exception) {
            false
        }
    }
    
    override fun getStoredToken(): String? {
        return try {
            kotlinx.coroutines.runBlocking { tokenManager.getAccessToken() }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun clearAuthData() {
        tokenManager.clearTokens()
        userDao.clearUsers()
    }
}