package com.cocido.formokaizen.domain.usecases.auth

import com.cocido.formokaizen.domain.entities.AuthToken
import com.cocido.formokaizen.domain.repository.AuthRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<Resource<AuthToken>> {
        if (email.isBlank()) {
            return kotlinx.coroutines.flow.flowOf(Resource.Error("Email es requerido"))
        }
        
        if (password.isBlank()) {
            return kotlinx.coroutines.flow.flowOf(Resource.Error("Contraseña es requerida"))
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return kotlinx.coroutines.flow.flowOf(Resource.Error("Email inválido"))
        }
        
        return authRepository.login(email, password)
    }
}