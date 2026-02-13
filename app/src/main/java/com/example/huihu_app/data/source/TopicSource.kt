package com.example.huihu_app.data.source

import com.example.huihu_app.data.model.ApiResponse
import com.example.huihu_app.data.model.Topic
import retrofit2.http.GET
import retrofit2.http.Query

interface TopicSource {
    @GET("/topic")
    suspend fun topics(@Query("page") page: Int): ApiResponse<List<Topic>>
}
