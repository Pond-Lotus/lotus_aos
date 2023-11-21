package com.example.todo_android.repository.Todo

import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.common.APIResponse
import com.example.todo_android.response.TodoResponse.CreateTodoResponse
import com.example.todo_android.response.TodoResponse.DeleteTodoResponse
import com.example.todo_android.response.TodoResponse.ReadTodoResponse
import com.example.todo_android.response.TodoResponse.UpdateTodoResponse


interface TodoRepository {
    suspend fun createTodo(token: String, createTodo: CreateTodo):
            APIResponse<CreateTodoResponse>
    suspend fun readTodo(token: String, year: Int, month: Int, day: Int):
            APIResponse<ReadTodoResponse>
    suspend fun updateTodo(token: String, id: String, updateTodo: UpdateTodo):
            APIResponse<UpdateTodoResponse>
    suspend fun deleteTodo(token: String, id: String):
            APIResponse<DeleteTodoResponse>
}