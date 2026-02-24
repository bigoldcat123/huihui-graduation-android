package com.example.huihu_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val id: Int,
    val restaurant_id: Int,
    val name: String,
    val description: String,
    val image: String,
    val price: Double? = null
)
