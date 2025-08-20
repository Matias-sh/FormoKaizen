package com.cocido.formokaizen.data.local.dao

import androidx.room.*
import com.cocido.formokaizen.data.local.entities.UserEntity
import com.cocido.formokaizen.data.local.entities.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    
    @Query("SELECT * FROM users WHERE isActive = 1 ORDER BY firstName, lastName")
    fun getAllUsers(): Flow<List<UserEntity>>
    
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?
    
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?
    
    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)
    
    @Update
    suspend fun updateUser(user: UserEntity)
    
    @Delete
    suspend fun deleteUser(user: UserEntity)
    
    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: Int)
    
    @Query("SELECT * FROM users WHERE syncStatus != :syncStatus")
    suspend fun getUsersWithSyncStatus(syncStatus: SyncStatus = SyncStatus.SYNCED): List<UserEntity>
    
    @Query("UPDATE users SET syncStatus = :newStatus WHERE id = :id")
    suspend fun updateUserSyncStatus(id: Int, newStatus: SyncStatus)
    
    @Query("DELETE FROM users")
    suspend fun clearUsers()
}