package com.cocido.formokaizen.domain.repositories

import com.cocido.formokaizen.domain.entities.*
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchTarjetas(
        filter: SearchFilter,
        page: Int = 0,
        pageSize: Int = 20
    ): Flow<Resource<SearchResult>>

    suspend fun getQuickSearchSuggestions(query: String): Flow<Resource<List<String>>>
    
    suspend fun getFilterOptions(): Flow<Resource<List<FilterGroup>>>
    
    suspend fun saveFilter(name: String, description: String?, filter: SearchFilter): Flow<Resource<SavedFilter>>
    
    suspend fun getSavedFilters(): Flow<Resource<List<SavedFilter>>>
    
    suspend fun deleteSavedFilter(filterId: Int): Flow<Resource<Unit>>
    
    suspend fun getPopularTags(): Flow<Resource<List<String>>>
    
    suspend fun getRecentSearches(): Flow<Resource<List<String>>>
    
    suspend fun saveSearch(query: String): Flow<Resource<Unit>>
}