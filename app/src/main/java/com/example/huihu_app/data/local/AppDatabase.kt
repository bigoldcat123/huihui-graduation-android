package com.example.huihu_app.data.local

import androidx.room.Database
import androidx.room.migration.Migration
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.huihu_app.data.local.dao.FoodCacheDao
import com.example.huihu_app.data.local.entity.FoodCacheEntity

@Database(
    entities = [FoodCacheEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodCacheDao(): FoodCacheDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE food_cache ADD COLUMN price REAL")
            }
        }
    }
}
