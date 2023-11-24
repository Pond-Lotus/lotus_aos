package com.example.todo_android.repository.Category

import com.example.todo_android.Data.Category.UpdateCategory
import com.example.todo_android.common.APIResponse
import com.example.todo_android.response.CategoryResponse.ReadCategoryResponse
import com.example.todo_android.response.CategoryResponse.UpdateCategoryResponse

interface CategoryRepository {
    suspend fun readTodoCategory(token: String):
            APIResponse<ReadCategoryResponse>

    suspend fun updateTodoCategory(token: String, category: UpdateCategory):
            APIResponse<UpdateCategoryResponse>
}