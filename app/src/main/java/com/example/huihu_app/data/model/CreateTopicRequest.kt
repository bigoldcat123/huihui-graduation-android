package com.example.huihu_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateTopicRequest(
    val title: String,
    val content: String,
    val images: List<String> = emptyList(),
    val reply_to_id: Int? = null,
    val location: String? = null,
    val is_public: Boolean
)
