package com.cocido.formokaizen.domain.usecases.notifications

import com.cocido.formokaizen.domain.repository.NotificationsRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MarkNotificationReadUseCase @Inject constructor(
    private val notificationsRepository: NotificationsRepository
) {
    suspend operator fun invoke(notificationId: Int): Flow<Resource<Unit>> {
        if (notificationId <= 0) {
            return flowOf(Resource.Error("ID de notificación inválido"))
        }
        
        return notificationsRepository.markNotificationAsRead(notificationId)
    }
}