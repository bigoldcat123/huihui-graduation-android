package com.example.huihu_app.data.repository

import com.example.huihu_app.data.model.ApiResponse
import com.example.huihu_app.data.model.ConsecutiveSuggestRequest
import com.example.huihu_app.data.model.Food
import com.example.huihu_app.data.source.FoodSource

class FoodRepository(
    private val foodSource: FoodSource,
) {
    suspend fun consecutiveSuggest(
        token: String,
        foodIds: List<Int>,
        selectedFoodIds: List<Int>
    ): ApiResponse<List<Food>> =
        runCatching {
            foodSource.consecutiveSuggest(
                token = "Bearer $token",
                request = ConsecutiveSuggestRequest(
                    food_ids = foodIds,
                    selected_food_ids = selectedFoodIds
                )
            )
        }.getOrElse {
            return ApiResponse.from(it)
        }
}
