package com.example.todo_android.Request.TodoRequest

import com.example.todo_android.Response.TodoResponse.ReadTodoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ReadTodoRequest {
    @GET("/todo/todo/")
    fun requestReadTodo(
        @Header("Authorization") token: String,
        @Query("year") year: String,
        @Query("month") month: String,
        @Query("day") day: String
        ): Call<ReadTodoResponse>
}