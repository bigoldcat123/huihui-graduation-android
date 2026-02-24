package com.example.huihu_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FoodTag(
    val id: Int,
    val name: String,
    val image: String? = null
)
