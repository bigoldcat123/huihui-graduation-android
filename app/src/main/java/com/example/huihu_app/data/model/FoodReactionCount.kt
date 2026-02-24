package com.example.huihu_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FoodReactionCount(
    val like: Int = 0,
    val dislike: Int = 0
)
