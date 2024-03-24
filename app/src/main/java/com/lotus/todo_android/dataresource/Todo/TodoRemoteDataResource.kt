package com.lotus.todo_android.dataresource.Todo

import com.lotus.todo_android.Data.Todo.CreateTodo
import com.lotus.todo_android.Data.Todo.UpdateTodo
import com.lotus.todo_android.response.TodoResponse.CreateTodoResponse
import com.lotus.todo_android.response.TodoResponse.DeleteTodoResponse
import com.lotus.todo_android.response.TodoResponse.ReadTodoResponse
import com.lotus.todo_android.response.TodoResponse.UpdateTodoResponse
import retrofit2.Call

interface TodoRemoteDataResource {
    suspend fun createTodo(token: String, createTodo: com.lotus.todo_android.Data.Todo.CreateTodo):
            Call<CreateTodoResponse>

    suspend fun readTodo(token: String, year: Int, month: Int, day: Int):
            Call<ReadTodoResponse>

    suspend fun updateTodo(token: String, id: String, updateTodo: com.lotus.todo_android.Data.Todo.UpdateTodo):
            Call<UpdateTodoResponse>

    suspend fun deleteTodo(token: String, id: String):
            Call<DeleteTodoResponse>
}