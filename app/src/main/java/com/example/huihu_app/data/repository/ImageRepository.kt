package com.example.huihu_app.data.repository

import com.example.huihu_app.data.model.ApiResponse
import com.example.huihu_app.data.source.ImageSource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ImageRepository(
    private val imageSource: ImageSource
) {
    suspend fun healthCheck(): ApiResponse<String> = runCatching {
        imageSource.healthCheck()
    }.getOrElse {
        return ApiResponse.from(it)
    }

    suspend fun insertImage(imageFile: File, calories: Int): ApiResponse<Unit?> = runCatching {
        val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
        imageSource.insertImage(imagePart, calories)
    }.getOrElse {
        return ApiResponse.from(it)
    }

    suspend fun searchImage(imageFile: File): ApiResponse<Int> = runCatching {
        val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
        imageSource.searchImage(imagePart)
    }.getOrElse {
        return ApiResponse.from(it)
    }
}
