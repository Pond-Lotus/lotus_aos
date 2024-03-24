package com.lotus.todo_android.repository.Category

import com.lotus.todo_android.common.APIResponse
import com.lotus.todo_android.response.CategoryResponse.ReadCategoryResponse
import com.lotus.todo_android.response.CategoryResponse.UpdateCategoryResponse

interface CategoryRepository {
    suspend fun readTodoCategory(token: String):
            com.lotus.todo_android.common.APIResponse<ReadCategoryResponse>

    suspend fun updateTodoCategory(token: String, category: Map<Int, String>):
            com.lotus.todo_android.common.APIResponse<UpdateCategoryResponse>
}