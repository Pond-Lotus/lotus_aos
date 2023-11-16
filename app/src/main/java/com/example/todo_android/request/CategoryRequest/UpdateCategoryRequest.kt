package com.example.todo_android.request.CategoryRequest

import com.example.todo_android.Data.Category.UpdateCategory
import com.example.todo_android.response.CategoryResponse.UpdateCategoryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UpdateCategoryRequest {
    @POST("/todo/name/priority/")
    fun requestUpdateCategory(
        @Header("Authorization") token: String,
        @Body updateCategory: UpdateCategory
    ): Call<UpdateCategoryResponse>
}