package com.example.todo_android.Request.TodoRequest

import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.Response.TodoResponse.UpdateTodoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT

interface UpdateTodoRequest {
    @PUT("/todo/todo/50/")
    fun requestUpdateTodo(
        @Header("Authorization") token: String,
//        @Path("id") id: String,
        @Body createUpdateRequest: UpdateTodo,
    ): Call<UpdateTodoResponse>
}