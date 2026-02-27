package com.example.huihu_app.data.local

import androidx.room.Database
import androidx.room.migration.Migration
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.huihu_app.data.local.dao.FoodCacheDao
import com.example.huihu_app.data.local.entity.FoodCacheEntity

@Database(
    entities = [FoodCacheEntity::class],
    version = 3,
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

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `food_cache_new` (
                        `id` INTEGER NOT NULL,
                        `isRandom` INTEGER NOT NULL DEFAULT 0,
                        `restaurantId` INTEGER NOT NULL,
                        `name` TEXT NOT NULL,
                        `description` TEXT NOT NULL,
                        `image` TEXT NOT NULL,
                        `price` REAL,
                        `createdAt` INTEGER NOT NULL,
                        PRIMARY KEY(`id`, `isRandom`)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO `food_cache_new` (
                        `id`, `isRandom`, `restaurantId`, `name`, `description`, `image`, `price`, `createdAt`
                    )
                    SELECT `id`, 0, `restaurantId`, `name`, `description`, `image`, `price`, `createdAt`
                    FROM `food_cache`
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE `food_cache`")
                db.execSQL("ALTER TABLE `food_cache_new` RENAME TO `food_cache`")
            }
        }
    }
}
