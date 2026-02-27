package com.example.huihu_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.huihu_app.data.local.entity.FoodCacheEntity

@Dao
interface FoodCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<FoodCacheEntity>)

    @Query("SELECT * FROM food_cache WHERE isRandom = :isRandom ORDER BY createdAt ASC")
    suspend fun getAllOrdered(isRandom: Boolean): List<FoodCacheEntity>

    @Query("SELECT * FROM food_cache WHERE isRandom = :isRandom ORDER BY createdAt ASC LIMIT 1")
    suspend fun getTopOne(isRandom: Boolean): FoodCacheEntity?

    @Query("SELECT COUNT(*) FROM food_cache WHERE isRandom = :isRandom")
    suspend fun count(isRandom: Boolean): Int

    @Query("DELETE FROM food_cache WHERE id = :id AND isRandom = :isRandom")
    suspend fun deleteById(id: Int, isRandom: Boolean)

    @Query(
        "DELETE FROM food_cache " +
            "WHERE isRandom = :isRandom " +
            "AND id NOT IN (" +
            "SELECT id FROM food_cache WHERE isRandom = :isRandom ORDER BY createdAt DESC LIMIT :limit" +
            ")"
    )
    suspend fun trimToLimit(limit: Int, isRandom: Boolean)

    @Query("DELETE FROM food_cache WHERE isRandom = :isRandom")
    suspend fun clearByMode(isRandom: Boolean)

    @Query("DELETE FROM food_cache")
    suspend fun clearAll()
}
