package com.example.todo_android.Request.TodoRequest

import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Response.TodoResponse.CreateTodoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CreateTodoRequest {
    @POST("/todo/todo/")
    fun requestCreateTodo(
        @Header("Authorization") token: String,
        @Body createTodoRequest : CreateTodo
    ): Call<CreateTodoResponse>

}