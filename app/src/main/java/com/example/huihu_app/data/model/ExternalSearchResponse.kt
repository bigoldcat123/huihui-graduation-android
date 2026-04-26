package com.example.huihu_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ExternalSearchResponse(
    val description: String,
    val cal: Long,
    val image_url: String,
    val food_name: String
)

data class RecognitionResult(
    val calories: Double,
    val foodName: String
)
