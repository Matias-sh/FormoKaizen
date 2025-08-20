package com.cocido.formokaizen.domain.usecases.auth

import com.cocido.formokaizen.domain.entities.AuthToken
import com.cocido.formokaizen.domain.entities.RegisterRequest
import com.cocido.formokaizen.domain.repository.AuthRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: RegisterRequest): Flow<Resource<AuthToken>> {
        // Validaciones
        if (request.username.isBlank()) {
            return flowOf(Resource.Error("Nombre de usuario es requerido"))
        }
        
        if (request.email.isBlank()) {
            return flowOf(Resource.Error("Email es requerido"))
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(request.email).matches()) {
            return flowOf(Resource.Error("Email inválido"))
        }
        
        if (request.password.isBlank()) {
            return flowOf(Resource.Error("Contraseña es requerida"))
        }
        
        if (request.password.length < 8) {
            return flowOf(Resource.Error("La contraseña debe tener al menos 8 caracteres"))
        }
        
        if (request.password != request.passwordConfirm) {
            return flowOf(Resource.Error("Las contraseñas no coinciden"))
        }
        
        if (request.firstName.isBlank()) {
            return flowOf(Resource.Error("Nombre es requerido"))
        }
        
        if (request.lastName.isBlank()) {
            return flowOf(Resource.Error("Apellido es requerido"))
        }
        
        return authRepository.register(request)
    }
}