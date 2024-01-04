package com.example.todo_android.dataresource.Category

import com.example.todo_android.response.CategoryResponse.ReadCategoryResponse
import com.example.todo_android.response.CategoryResponse.UpdateCategoryResponse
import com.example.todo_android.service.Category.CategoryService
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRemoteDataResourceImpl @Inject constructor(
    private val categoryService: CategoryService
) : CategoryRemoteDataResource {
    override suspend fun readTodoCategory(token: String): Call<ReadCategoryResponse> {
        return categoryService.requestReadCategory(token)
    }

    override suspend fun updateTodoCategory(
        token: String,
        category: Map<Int, String>
    ): Call<UpdateCategoryResponse> {
        return categoryService.requestUpdateCategory(token, category)
    }
}