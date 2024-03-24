package com.lotus.todo_android.request.TodoRequest

import com.lotus.todo_android.Data.Todo.UpdateTodo
import com.lotus.todo_android.response.TodoResponse.UpdateTodoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface UpdateTodoRequest {
    @PUT("/todo/todo/{id}/")
    fun requestUpdateTodo(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body createUpdateRequest: com.lotus.todo_android.Data.Todo.UpdateTodo,
    ): Call<UpdateTodoResponse>
}