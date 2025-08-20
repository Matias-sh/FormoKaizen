package com.cocido.formokaizen.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    @SerializedName("password_confirm")
    val passwordConfirm: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("employee_id")
    val employeeId: String?,
    val phone: String?,
    val department: String?,
    val position: String?
)

data class AuthResponse(
    val user: UserDto,
    val access: String,
    val refresh: String
)

data class RefreshTokenRequest(
    val refresh: String
)

data class ChangePasswordRequest(
    @SerializedName("current_password")
    val currentPassword: String,
    @SerializedName("new_password")
    val newPassword: String,
    @SerializedName("new_password_confirm")
    val newPasswordConfirm: String
)