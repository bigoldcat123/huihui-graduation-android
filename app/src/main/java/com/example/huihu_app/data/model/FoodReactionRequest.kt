package com.example.huihu_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FoodReactionRequest(
    val food_id: Int,
    val reaction: String,
    val source: String,
    val occurred_at: Long
)
