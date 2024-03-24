package com.lotus.todo_android.request.TodoRequest

import com.lotus.todo_android.Data.Todo.CreateTodo
import com.lotus.todo_android.response.TodoResponse.CreateTodoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CreateTodoRequest {
    @POST("/todo/todo/")
    fun requestCreateTodo(
        @Header("Authorization") token: String,
        @Body createTodoRequest : com.lotus.todo_android.Data.Todo.CreateTodo
    ): Call<CreateTodoResponse>

}