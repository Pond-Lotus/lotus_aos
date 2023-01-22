package com.example.todo_android.Request.TodoRequest

import com.example.todo_android.Data.Todo.UpdateTodo
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT

interface UpdateTodoRequest {
    @PUT("/todo/todo/{id}/")
    fun requestUpdateTodo(
        @Header("Authorization") token: String,
        @Body createUpdateRequest : UpdateTodo
    )




}