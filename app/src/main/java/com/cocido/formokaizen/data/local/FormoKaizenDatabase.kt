package com.cocido.formokaizen.data.local

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cocido.formokaizen.data.local.dao.*
import com.cocido.formokaizen.data.local.entities.*

@Database(
    entities = [
        UserEntity::class,
        TarjetaRojaEntity::class,
        TarjetaImageEntity::class,
        TarjetaCommentEntity::class,
        TarjetaHistoryEntity::class,
        PendingOperationEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FormoKaizenDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun tarjetasDao(): TarjetasDao
    
    companion object {
        const val DATABASE_NAME = "formokaizen_database"
        
        // Migration from version 1 to 2: Update TarjetaRoja structure
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Drop old tarjetas_rojas table and recreate with new structure
                database.execSQL("DROP TABLE IF EXISTS tarjetas_rojas")
                
                // Create new tarjetas_rojas table
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS tarjetas_rojas (
                        id INTEGER PRIMARY KEY NOT NULL,
                        numero TEXT NOT NULL,
                        fecha TEXT NOT NULL,
                        sector TEXT NOT NULL,
                        descripcion TEXT NOT NULL,
                        motivo TEXT NOT NULL,
                        quienLoHizo TEXT NOT NULL,
                        destinoFinal TEXT NOT NULL,
                        fechaFinal TEXT,
                        createdById INTEGER NOT NULL,
                        assignedToId INTEGER,
                        approvedById INTEGER,
                        status TEXT NOT NULL,
                        priority TEXT NOT NULL,
                        createdAt TEXT NOT NULL,
                        updatedAt TEXT NOT NULL,
                        approvedAt TEXT,
                        closedAt TEXT,
                        isOverdue INTEGER NOT NULL,
                        daysOpen INTEGER NOT NULL,
                        syncStatus TEXT NOT NULL,
                        lastModified INTEGER NOT NULL,
                        FOREIGN KEY(createdById) REFERENCES users(id) ON DELETE CASCADE,
                        FOREIGN KEY(assignedToId) REFERENCES users(id) ON DELETE SET NULL,
                        FOREIGN KEY(approvedById) REFERENCES users(id) ON DELETE SET NULL
                    )
                """.trimIndent())
                
                // Create indexes
                database.execSQL("CREATE INDEX IF NOT EXISTS index_tarjetas_rojas_createdById ON tarjetas_rojas(createdById)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_tarjetas_rojas_assignedToId ON tarjetas_rojas(assignedToId)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_tarjetas_rojas_approvedById ON tarjetas_rojas(approvedById)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_tarjetas_rojas_status ON tarjetas_rojas(status)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_tarjetas_rojas_priority ON tarjetas_rojas(priority)")
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_tarjetas_rojas_numero ON tarjetas_rojas(numero)")
                
                // Drop categories and work_areas tables if they exist (no longer needed)
                database.execSQL("DROP TABLE IF EXISTS categories")
                database.execSQL("DROP TABLE IF EXISTS work_areas")
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