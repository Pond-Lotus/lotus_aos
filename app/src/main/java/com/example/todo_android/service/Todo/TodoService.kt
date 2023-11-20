package com.example.todo_android.service.Todo

import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.response.TodoResponse.CreateTodoResponse
import com.example.todo_android.response.TodoResponse.DeleteTodoResponse
import com.example.todo_android.response.TodoResponse.ReadTodoResponse
import com.example.todo_android.response.TodoResponse.UpdateTodoResponse
import retrofit2.Call
import retrofit2.http.*

interface TodoService {

    @GET("/todo/todo/")
    fun requestReadTodo(
        @Header("Authorization") token: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int
    ): Call<ReadTodoResponse>

    @POST("/todo/todo/")
    fun requestCreateTodo(
        @Header("Authorization") token: String,
        @Body createTodoRequest : CreateTodo
    ): Call<CreateTodoResponse>

    @PUT("/todo/todo/{id}/")
    fun requestUpdateTodo(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body createUpdateRequest: UpdateTodo,
    ): Call<UpdateTodoResponse>

    @DELETE("/todo/todo/{id}/")
    fun requestDeleteTodo(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DeleteTodoResponse>

}