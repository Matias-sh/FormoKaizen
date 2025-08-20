package com.cocido.formokaizen.domain.usecases.auth

import com.cocido.formokaizen.domain.entities.User
import com.cocido.formokaizen.domain.repository.AuthRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Flow<Resource<User>> {
        return authRepository.getCurrentUser()
    }
}