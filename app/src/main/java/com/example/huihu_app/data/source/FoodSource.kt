package com.example.huihu_app.data.source

import com.example.huihu_app.data.model.ApiResponse
import com.example.huihu_app.data.model.ConsecutiveSuggestRequest
import com.example.huihu_app.data.model.Food
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FoodSource {
    @POST("/food/consecutiveSuggest")
    suspend fun consecutiveSuggest(
        @Header("Authorization") token: String,
        @Body request: ConsecutiveSuggestRequest
    ): ApiResponse<List<Food>>
}
