package com.cocido.formokaizen.domain.repositories

import com.cocido.formokaizen.domain.entities.UpdateSettingsRequest
import com.cocido.formokaizen.domain.entities.UserSettings
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    suspend fun getUserSettings(): Flow<Resource<UserSettings>>
    
    suspend fun updateUserSettings(request: UpdateSettingsRequest): Flow<Resource<UserSettings>>
    
    suspend fun resetToDefaults(): Flow<Resource<UserSettings>>
    
    suspend fun exportSettings(): Flow<Resource<String>>
    
    suspend fun importSettings(settingsJson: String): Flow<Resource<UserSettings>>
}