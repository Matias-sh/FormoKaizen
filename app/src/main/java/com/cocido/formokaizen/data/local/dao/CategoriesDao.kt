package com.cocido.formokaizen.data.local.dao

import androidx.room.*
import com.cocido.formokaizen.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {
    
    @Transaction
    @Query("SELECT * FROM categories WHERE isActive = 1 ORDER BY name")
    fun getAllCategories(): Flow<List<CategoryWithWorkAreas>>
    
    @Transaction
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryWithWorkAreas?
    
    @Query("SELECT * FROM categories WHERE isActive = 1 ORDER BY name")
    fun getCategoriesSimple(): Flow<List<CategoryEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)
    
    @Update
    suspend fun updateCategory(category: CategoryEntity)
    
    @Delete
    suspend fun deleteCategory(category: CategoryEntity)
    
    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: Int)
    
    // Work Areas
    @Query("SELECT * FROM work_areas WHERE categoryId = :categoryId AND isActive = 1 ORDER BY name")
    fun getWorkAreasByCategory(categoryId: Int): Flow<List<WorkAreaEntity>>
    
    @Query("SELECT * FROM work_areas WHERE id = :id")
    suspend fun getWorkAreaById(id: Int): WorkAreaEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkArea(workArea: WorkAreaEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkAreas(workAreas: List<WorkAreaEntity>)
    
    @Update
    suspend fun updateWorkArea(workArea: WorkAreaEntity)
    
    @Delete
    suspend fun deleteWorkArea(workArea: WorkAreaEntity)
    
    @Query("DELETE FROM work_areas WHERE id = :id")
    suspend fun deleteWorkAreaById(id: Int)
    
    // Sync operations
    @Query("SELECT * FROM categories WHERE syncStatus != 'SYNCED'")
    suspend fun getCategoriesWithPendingSync(): List<CategoryEntity>
    
    @Query("SELECT * FROM work_areas WHERE syncStatus != 'SYNCED'")
    suspend fun getWorkAreasWithPendingSync(): List<WorkAreaEntity>
    
    @Query("UPDATE categories SET syncStatus = :newStatus WHERE id = :id")
    suspend fun updateCategorySyncStatus(id: Int, newStatus: SyncStatus)
    
    @Query("UPDATE work_areas SET syncStatus = :newStatus WHERE id = :id")
    suspend fun updateWorkAreaSyncStatus(id: Int, newStatus: SyncStatus)
    
    // Statistics
    @Query("SELECT COUNT(*) FROM categories WHERE isActive = 1")
    suspend fun getCategoriesCount(): Int
    
    @Query("SELECT COUNT(*) FROM work_areas WHERE categoryId = :categoryId AND isActive = 1")
    suspend fun getWorkAreasCountByCategory(categoryId: Int): Int
    
    @Query("DELETE FROM categories")
    suspend fun clearCategories()
    
    @Query("DELETE FROM work_areas")
    suspend fun clearWorkAreas()
}