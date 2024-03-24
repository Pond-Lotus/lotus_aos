package com.lotus.todo_android.dataresource.Category

import com.lotus.todo_android.response.CategoryResponse.ReadCategoryResponse
import com.lotus.todo_android.response.CategoryResponse.UpdateCategoryResponse
import retrofit2.Call

interface CategoryRemoteDataResource {
    suspend fun readTodoCategory(token: String):
            Call<ReadCategoryResponse>

    suspend fun updateTodoCategory(token: String, category: Map<Int, String>):
            Call<UpdateCategoryResponse>
}