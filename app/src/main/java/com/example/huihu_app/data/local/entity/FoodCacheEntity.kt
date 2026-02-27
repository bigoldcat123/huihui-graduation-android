package com.example.huihu_app.data.local.entity

import androidx.room.Entity
import com.example.huihu_app.data.model.Food

@Entity(
    tableName = "food_cache",
    primaryKeys = ["id", "isRandom"]
)
data class FoodCacheEntity(
    val id: Int,
    val isRandom: Boolean = false,
    val restaurantId: Int,
    val name: String,
    val description: String,
    val image: String,
    val price: Double? = null,
    val createdAt: Long
)

fun Food.toEntity(
    isRandom: Boolean,
    now: Long = System.currentTimeMillis()
): FoodCacheEntity =
    FoodCacheEntity(
        id = id,
        isRandom = isRandom,
        restaurantId = restaurant_id,
        name = name,
        description = description,
        image = image,
        price = price,
        createdAt = now
    )

fun FoodCacheEntity.toFood(): Food =
    Food(
        id = id,
        restaurant_id = restaurantId,
        name = name,
        description = description,
        image = image,
        price = price
    )
