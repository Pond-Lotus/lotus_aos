package com.lotus.todo_android.request.TodoRequest

import com.lotus.todo_android.response.TodoResponse.ReadTodoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ReadTodoRequest {
    @GET("/todo/todo/")
    fun requestReadTodo(
        @Header("Authorization") token: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int
        ): Call<ReadTodoResponse>
}