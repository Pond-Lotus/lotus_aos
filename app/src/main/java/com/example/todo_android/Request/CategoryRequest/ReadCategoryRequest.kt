package com.example.todo_android.Request.CategoryRequest

import com.example.todo_android.Data.Category.ReadCategory
import com.example.todo_android.Response.CategoryResponse.ReadCategoryResponse
import com.example.todo_android.Response.TodoResponse.ReadTodoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ReadCategoryRequest {
    @GET("/todo/name/priority/")
    fun requestReadCategory(
        @Header("Authorization") token: String
    ): Call<ReadCategoryResponse>
}