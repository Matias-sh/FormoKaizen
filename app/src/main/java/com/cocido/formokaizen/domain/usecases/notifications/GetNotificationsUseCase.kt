package com.cocido.formokaizen.domain.usecases.notifications

import com.cocido.formokaizen.domain.entities.Notification
import com.cocido.formokaizen.domain.repository.NotificationsRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val notificationsRepository: NotificationsRepository
) {
    suspend operator fun invoke(
        page: Int = 1,
        perPage: Int = 50,
        unreadOnly: Boolean = false,
        type: String? = null,
        priority: String? = null
    ): Flow<Resource<List<Notification>>> {
        return notificationsRepository.getNotifications(
            page = page,
            perPage = perPage,
            unreadOnly = unreadOnly,
            type = type,
            priority = priority
        )
    }
}