package com.example.todo_android.dataresource.Todo

import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.response.TodoResponse.CreateTodoResponse
import com.example.todo_android.response.TodoResponse.DeleteTodoResponse
import com.example.todo_android.response.TodoResponse.ReadTodoResponse
import com.example.todo_android.response.TodoResponse.UpdateTodoResponse
import retrofit2.Call

interface TodoRemoteDataResource {
    suspend fun createTodo(token: String, createTodo: CreateTodo):
            Call<CreateTodoResponse>

    suspend fun readTodo(token: String, year: Int, month: Int, day: Int):
            Call<ReadTodoResponse>

    suspend fun updateTodo(token: String, id: String, updateTodo: UpdateTodo):
            Call<UpdateTodoResponse>

    suspend fun deleteTodo(token: String, id: String):
            Call<DeleteTodoResponse>
}