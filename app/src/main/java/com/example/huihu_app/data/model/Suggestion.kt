package com.example.huihu_app.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@JsonIgnoreUnknownKeys
@Serializable
data class Suggestion(
    val id: Int,
    val content: String,
    val status: String,
    val created_at: String
)
