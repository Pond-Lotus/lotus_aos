package com.example.todo_android.Request.TodoRequest

import com.example.todo_android.Response.TodoResponse.DeleteTodoResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Header

interface DeleteTodoRequest {
    @DELETE("/todo/todo/93/")
    fun requestDeleteTodo(
        @Header("Authorization") token: String
    ): Call<DeleteTodoResponse>
}