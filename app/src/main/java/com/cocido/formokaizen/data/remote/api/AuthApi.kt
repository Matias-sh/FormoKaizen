package com.cocido.formokaizen.data.remote.api

import com.cocido.formokaizen.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    
    @POST("auth/login/")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("auth/register/")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("auth/refresh/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<AuthResponse>
    
    @GET("auth/profile/")
    suspend fun getCurrentUser(): Response<UserDto>
    
    @PUT("auth/profile/update/")
    suspend fun updateProfile(@Body user: UserDto): Response<ApiResponse<UserDto>>
    
    @POST("auth/change-password/")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<ApiResponse<Unit>>
    
    @GET("auth/users/")
    suspend fun getUsersList(): Response<List<UserDto>>
}

data class ApiResponse<T>(
    val message: String,
    val user: T? = null,
    val data: T? = null
)