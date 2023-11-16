package com.example.todo_android.request.TodoRequest

import com.example.todo_android.response.TodoResponse.DeleteTodoResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Path

interface DeleteTodoRequest {
    @DELETE("/todo/todo/{id}/")
    fun requestDeleteTodo(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DeleteTodoResponse>
}