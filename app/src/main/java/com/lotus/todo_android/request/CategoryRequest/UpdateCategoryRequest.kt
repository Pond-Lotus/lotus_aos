package com.lotus.todo_android.request.CategoryRequest

import com.lotus.todo_android.Data.Category.UpdateCategory
import com.lotus.todo_android.response.CategoryResponse.UpdateCategoryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UpdateCategoryRequest {
    @POST("/todo/name/priority/")
    fun requestUpdateCategory(
        @Header("Authorization") token: String,
        @Body updateCategory: com.lotus.todo_android.Data.Category.UpdateCategory
    ): Call<UpdateCategoryResponse>
}