package com.cocido.formokaizen.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cocido.formokaizen.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT COUNT(*) FROM users")
    suspend fun userCount(): Int

    @Insert
    suspend fun insert(user: User)
}
