package com.cocido.formokaizen.data.local.dao

import androidx.room.*
import com.cocido.formokaizen.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TarjetasDao {
    
    @Transaction
    @Query("""
        SELECT * FROM tarjetas_rojas 
        ORDER BY 
            CASE WHEN status = 'PENDING_APPROVAL' THEN 1
                 WHEN status = 'OPEN' THEN 2
                 WHEN status = 'IN_PROGRESS' THEN 3
                 ELSE 4 END,
            CASE WHEN priority = 'CRITICAL' THEN 1
                 WHEN priority = 'HIGH' THEN 2
                 WHEN priority = 'MEDIUM' THEN 3
                 ELSE 4 END,
            createdAt DESC
    """)
    fun getAllTarjetas(): Flow<List<TarjetaRojaWithRelations>>
    
    @Transaction
    @Query("SELECT * FROM tarjetas_rojas WHERE id = :id")
    suspend fun getTarjetaById(id: Int): TarjetaRojaWithRelations?
    
    @Transaction
    @Query("SELECT * FROM tarjetas_rojas WHERE status = :status ORDER BY createdAt DESC")
    fun getTarjetasByStatus(status: String): Flow<List<TarjetaRojaWithRelations>>
    
    @Transaction
    @Query("SELECT * FROM tarjetas_rojas WHERE priority = :priority ORDER BY createdAt DESC")
    fun getTarjetasByPriority(priority: String): Flow<List<TarjetaRojaWithRelations>>
    
    @Transaction
    @Query("SELECT * FROM tarjetas_rojas WHERE sector LIKE '%' || :sector || '%' ORDER BY createdAt DESC")
    fun getTarjetasBySector(sector: String): Flow<List<TarjetaRojaWithRelations>>
    
    @Transaction
    @Query("SELECT * FROM tarjetas_rojas WHERE assignedToId = :userId ORDER BY createdAt DESC")
    fun getTarjetasAssignedTo(userId: Int): Flow<List<TarjetaRojaWithRelations>>
    
    @Transaction
    @Query("SELECT * FROM tarjetas_rojas WHERE createdById = :userId ORDER BY createdAt DESC")
    fun getTarjetasCreatedBy(userId: Int): Flow<List<TarjetaRojaWithRelations>>
    
    @Transaction
    @Query("""
        SELECT * FROM tarjetas_rojas 
        WHERE numero LIKE '%' || :search || '%' 
           OR descripcion LIKE '%' || :search || '%' 
           OR sector LIKE '%' || :search || '%'
           OR motivo LIKE '%' || :search || '%'
           OR quienLoHizo LIKE '%' || :search || '%'
        ORDER BY createdAt DESC
    """)
    fun searchTarjetas(search: String): Flow<List<TarjetaRojaWithRelations>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTarjeta(tarjeta: TarjetaRojaEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTarjetas(tarjetas: List<TarjetaRojaEntity>)
    
    @Update
    suspend fun updateTarjeta(tarjeta: TarjetaRojaEntity)
    
    @Delete
    suspend fun deleteTarjeta(tarjeta: TarjetaRojaEntity)
    
    @Query("DELETE FROM tarjetas_rojas WHERE id = :id")
    suspend fun deleteTarjetaById(id: Int)
    
    // Image operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: TarjetaImageEntity)
    
    @Query("SELECT * FROM tarjeta_images WHERE tarjetaId = :tarjetaId")
    suspend fun getImagesByTarjetaId(tarjetaId: Int): List<TarjetaImageEntity>
    
    @Delete
    suspend fun deleteImage(image: TarjetaImageEntity)
    
    // Comment operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: TarjetaCommentEntity)
    
    @Query("SELECT * FROM tarjeta_comments WHERE tarjetaId = :tarjetaId ORDER BY createdAt ASC")
    suspend fun getCommentsByTarjetaId(tarjetaId: Int): List<TarjetaCommentEntity>
    
    @Delete
    suspend fun deleteComment(comment: TarjetaCommentEntity)
    
    // History operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: TarjetaHistoryEntity)
    
    @Query("SELECT * FROM tarjeta_history WHERE tarjetaId = :tarjetaId ORDER BY timestamp DESC")
    suspend fun getHistoryByTarjetaId(tarjetaId: Int): List<TarjetaHistoryEntity>
    
    // Sync operations
    @Query("SELECT * FROM tarjetas_rojas WHERE syncStatus != 'SYNCED'")
    suspend fun getTarjetasWithPendingSync(): List<TarjetaRojaEntity>
    
    @Query("UPDATE tarjetas_rojas SET syncStatus = :newStatus WHERE id = :id")
    suspend fun updateTarjetaSyncStatus(id: Int, newStatus: SyncStatus)
    
    // Statistics
    @Query("SELECT COUNT(*) FROM tarjetas_rojas")
    suspend fun getTotalTarjetasCount(): Int
    
    @Query("SELECT COUNT(*) FROM tarjetas_rojas WHERE status = :status")
    suspend fun getTarjetasCountByStatus(status: String): Int
    
    @Query("SELECT COUNT(*) FROM tarjetas_rojas WHERE priority = :priority")
    suspend fun getTarjetasCountByPriority(priority: String): Int
    
    @Query("SELECT COUNT(*) FROM tarjetas_rojas WHERE sector = :sector")
    suspend fun getTarjetasCountBySector(sector: String): Int
    
    @Query("""
        SELECT COUNT(*) FROM tarjetas_rojas 
        WHERE isOverdue = 1 AND status NOT IN ('CLOSED', 'RESOLVED')
    """)
    suspend fun getOverdueTarjetasCount(): Int
    
    @Query("DELETE FROM tarjetas_rojas")
    suspend fun clearTarjetas()
    
    @Query("DELETE FROM tarjeta_images")
    suspend fun clearImages()
    
    @Query("DELETE FROM tarjeta_comments")
    suspend fun clearComments()
    
    @Query("DELETE FROM tarjeta_history")
    suspend fun clearHistory()
    
    // Check if numero already exists
    @Query("SELECT COUNT(*) FROM tarjetas_rojas WHERE numero = :numero")
    suspend fun existsByNumero(numero: String): Int
    
    @Query("SELECT COUNT(*) FROM tarjetas_rojas WHERE numero = :numero AND id != :excludeId")
    suspend fun existsByNumeroExcludingId(numero: String, excludeId: Int): Int
}