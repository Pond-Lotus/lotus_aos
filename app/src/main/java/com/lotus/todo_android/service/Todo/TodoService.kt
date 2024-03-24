package com.lotus.todo_android.service.Todo

import com.lotus.todo_android.Data.Todo.CreateTodo
import com.lotus.todo_android.Data.Todo.UpdateTodo
import com.lotus.todo_android.response.TodoResponse.CreateTodoResponse
import com.lotus.todo_android.response.TodoResponse.DeleteTodoResponse
import com.lotus.todo_android.response.TodoResponse.ReadTodoResponse
import com.lotus.todo_android.response.TodoResponse.UpdateTodoResponse
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
        @Body createTodoRequest : com.lotus.todo_android.Data.Todo.CreateTodo
    ): Call<CreateTodoResponse>

    @PUT("/todo/todo/{id}/")
    fun requestUpdateTodo(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body createUpdateRequest: com.lotus.todo_android.Data.Todo.UpdateTodo,
    ): Call<UpdateTodoResponse>

    @DELETE("/todo/todo/{id}/")
    fun requestDeleteTodo(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DeleteTodoResponse>

}