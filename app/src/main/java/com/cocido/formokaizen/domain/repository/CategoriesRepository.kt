package com.cocido.formokaizen.domain.repository

import com.cocido.formokaizen.domain.entities.Category
import com.cocido.formokaizen.domain.entities.WorkArea
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {
    suspend fun getCategories(): Flow<Resource<List<Category>>>
    suspend fun getAllCategories(): Flow<Resource<List<Category>>> // Alias para compatibilidad
    suspend fun getCategoryById(id: Int): Flow<Resource<Category>>
    suspend fun createCategory(category: Category): Flow<Resource<Category>>
    suspend fun updateCategory(category: Category): Flow<Resource<Category>>
    suspend fun deleteCategory(id: Int): Flow<Resource<Unit>>
    
    suspend fun getWorkAreas(categoryId: Int): Flow<Resource<List<WorkArea>>>
    suspend fun createWorkArea(categoryId: Int, workArea: WorkArea): Flow<Resource<WorkArea>>
    
    // Offline support
    fun getCategoriesOffline(): Flow<List<Category>>
    suspend fun syncCategories(): Flow<Resource<Unit>>
}