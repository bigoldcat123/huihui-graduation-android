package com.example.huihu_app.data.repository

import com.example.huihu_app.data.model.ApiResponse
import com.example.huihu_app.data.model.Suggestion
import com.example.huihu_app.data.source.SuggestionSource

class SuggestionRepository(
    private val suggestionSource: SuggestionSource
) {
    suspend fun mySuggestions(token: String): ApiResponse<List<Suggestion>> =
        runCatching {
            suggestionSource.mySuggestions("Bearer $token")
        }.getOrElse {
            return ApiResponse.from(it)
        }
}
