package com.cocido.formokaizen.domain.entities

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val passwordConfirm: String,
    val firstName: String,
    val lastName: String,
    val employeeId: String?,
    val phone: String?,
    val department: String?,
    val position: String?
)