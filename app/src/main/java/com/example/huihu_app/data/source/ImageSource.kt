package com.example.huihu_app.data.source

import com.example.huihu_app.data.model.ApiResponse
import com.example.huihu_app.data.model.ExternalSearchResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageSource {
    @GET("/image")
    suspend fun healthCheck(): ApiResponse<String>

    @Multipart
    @POST("/image/insert")
    suspend fun insertImage(
        @Part image: MultipartBody.Part,
        @Part("cal") cal: Int
    ): ApiResponse<Unit?>

    @Multipart
    @POST("/image")
    suspend fun searchImage(
        @Part image: MultipartBody.Part
    ): ApiResponse<ExternalSearchResponse>
}
