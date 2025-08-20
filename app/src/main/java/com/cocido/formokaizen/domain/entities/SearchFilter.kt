package com.cocido.formokaizen.domain.entities

import java.time.LocalDateTime

data class SearchFilter(
    val query: String? = null,
    val status: List<TarjetaStatus> = emptyList(),
    val priority: List<TarjetaPriority> = emptyList(),
    val categories: List<Int> = emptyList(),
    val assignedToIds: List<Int> = emptyList(),
    val createdByIds: List<Int> = emptyList(),
    val dateFrom: LocalDateTime? = null,
    val dateTo: LocalDateTime? = null,
    val dueDateFrom: LocalDateTime? = null,
    val dueDateTo: LocalDateTime? = null,
    val sectors: List<String> = emptyList(),
    val workAreas: List<Int> = emptyList(),
    val tags: List<String> = emptyList(),
    val hasAttachments: Boolean? = null,
    val isOverdue: Boolean? = null,
    val sortBy: SortBy = SortBy.CREATED_AT,
    val sortOrder: SortOrder = SortOrder.DESC
)

enum class SortBy {
    CREATED_AT,
    UPDATED_AT,
    DUE_DATE,
    PRIORITY,
    STATUS,
    TITLE,
    ASSIGNED_TO
}

enum class SortOrder {
    ASC,
    DESC
}

data class SearchResult(
    val tarjetas: List<TarjetaRoja>,
    val totalCount: Int,
    val hasMore: Boolean,
    val page: Int,
    val pageSize: Int
)

data class FilterOption(
    val id: String,
    val label: String,
    val count: Int = 0,
    val isSelected: Boolean = false
)

data class FilterGroup(
    val title: String,
    val key: String,
    val options: List<FilterOption>,
    val isMultiSelect: Boolean = true,
    val isExpanded: Boolean = false
)

data class SavedFilter(
    val id: Int,
    val name: String,
    val description: String?,
    val filter: SearchFilter,
    val isPublic: Boolean,
    val createdById: Int,
    val createdAt: LocalDateTime,
    val usageCount: Int = 0
)