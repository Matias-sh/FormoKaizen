package com.cocido.formokaizen.domain.entities

enum class SyncStatus {
    SYNCED,           // Data is synchronized with server
    PENDING_SYNC,     // Data needs to be sent to server
    SYNC_FAILED,      // Sync attempt failed
    PENDING_UPDATE,   // Local changes waiting to be synced
    PENDING_DELETE    // Marked for deletion, waiting sync
}

data class SyncInfo(
    val status: SyncStatus,
    val lastSyncAttempt: Long? = null,
    val syncError: String? = null,
    val retryCount: Int = 0
)