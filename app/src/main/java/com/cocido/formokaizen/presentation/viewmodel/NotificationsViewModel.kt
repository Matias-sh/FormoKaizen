package com.cocido.formokaizen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocido.formokaizen.domain.entities.Notification
import com.cocido.formokaizen.domain.repository.NotificationsRepository
import com.cocido.formokaizen.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {
    
    private val _notifications = MutableStateFlow<Resource<List<Notification>>>(Resource.Idle())
    val notifications: StateFlow<Resource<List<Notification>>> = _notifications.asStateFlow()
    
    private val _markAsReadState = MutableStateFlow<Resource<Unit>>(Resource.Idle())
    val markAsReadState: StateFlow<Resource<Unit>> = _markAsReadState.asStateFlow()
    
    private val _deleteState = MutableStateFlow<Resource<Unit>>(Resource.Idle())
    val deleteState: StateFlow<Resource<Unit>> = _deleteState.asStateFlow()
    
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()
    
    init {
        loadNotifications()
    }
    
    fun loadNotifications(
        page: Int = 1,
        perPage: Int = 50,
        unreadOnly: Boolean = false,
        type: String? = null,
        priority: String? = null
    ) {
        viewModelScope.launch {
            notificationsRepository.getNotifications(
                page = page,
                perPage = perPage,
                unreadOnly = unreadOnly,
                type = type,
                priority = priority
            ).collect { resource ->
                _notifications.value = resource
                
                // Update unread count
                if (resource is Resource.Success) {
                    val unreadCount = resource.data?.count { !it.isRead } ?: 0
                    _unreadCount.value = unreadCount
                }
            }
        }
    }
    
    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            notificationsRepository.markNotificationAsRead(notificationId).collect { resource ->
                _markAsReadState.value = resource
                if (resource is Resource.Success) {
                    // Reload notifications to update UI
                    loadNotifications()
                }
            }
        }
    }
    
    fun markAllAsRead() {
        viewModelScope.launch {
            notificationsRepository.markAllNotificationsAsRead().collect { resource ->
                _markAsReadState.value = resource
                if (resource is Resource.Success) {
                    // Reload notifications to update UI
                    loadNotifications()
                }
            }
        }
    }
    
    fun deleteNotification(notificationId: Int) {
        viewModelScope.launch {
            notificationsRepository.deleteNotification(notificationId).collect { resource ->
                _deleteState.value = resource
                if (resource is Resource.Success) {
                    // Reload notifications to update UI
                    loadNotifications()
                }
            }
        }
    }
    
    fun clearAllNotifications() {
        viewModelScope.launch {
            notificationsRepository.clearAllNotifications().collect { resource ->
                _deleteState.value = resource
                if (resource is Resource.Success) {
                    // Reload notifications to update UI
                    loadNotifications()
                }
            }
        }
    }
    
    fun filterByType(type: String?) {
        loadNotifications(type = type)
    }
    
    fun filterByUnreadOnly(unreadOnly: Boolean) {
        loadNotifications(unreadOnly = unreadOnly)
    }
    
    fun resetStates() {
        _markAsReadState.value = Resource.Idle()
        _deleteState.value = Resource.Idle()
    }
}