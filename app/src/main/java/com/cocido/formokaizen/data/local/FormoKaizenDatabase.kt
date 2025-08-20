package com.cocido.formokaizen.data.local

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cocido.formokaizen.data.local.dao.*
import com.cocido.formokaizen.data.local.entities.*

@Database(
    entities = [
        UserEntity::class,
        CategoryEntity::class,
        WorkAreaEntity::class,
        TarjetaRojaEntity::class,
        TarjetaImageEntity::class,
        TarjetaCommentEntity::class,
        TarjetaHistoryEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FormoKaizenDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun tarjetasDao(): TarjetasDao
    
    companion object {
        const val DATABASE_NAME = "formokaizen_database"
        
        // Migration example for future versions
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add new column or table changes here
                // database.execSQL("ALTER TABLE users ADD COLUMN new_field TEXT")
            }
        }
    }
}

class Converters {
    
    @TypeConverter
    fun fromSyncStatus(value: SyncStatus): String {
        return value.name
    }
    
    @TypeConverter
    fun toSyncStatus(value: String): SyncStatus {
        return try {
            SyncStatus.valueOf(value)
        } catch (e: IllegalArgumentException) {
            SyncStatus.SYNCED
        }
    }
    
}