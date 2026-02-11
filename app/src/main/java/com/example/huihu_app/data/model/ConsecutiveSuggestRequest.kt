package com.example.huihu_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ConsecutiveSuggestRequest(
    val food_ids: List<Int>,
    val selected_food_ids: List<Int>
)
