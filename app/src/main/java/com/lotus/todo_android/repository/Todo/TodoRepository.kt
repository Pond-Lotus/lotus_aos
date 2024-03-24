package com.lotus.todo_android.repository.Todo

import com.lotus.todo_android.Data.Todo.CreateTodo
import com.lotus.todo_android.Data.Todo.UpdateTodo
import com.lotus.todo_android.common.APIResponse
import com.lotus.todo_android.response.TodoResponse.CreateTodoResponse
import com.lotus.todo_android.response.TodoResponse.DeleteTodoResponse
import com.lotus.todo_android.response.TodoResponse.ReadTodoResponse
import com.lotus.todo_android.response.TodoResponse.UpdateTodoResponse


interface TodoRepository {
    suspend fun createTodo(token: String, createTodo: com.lotus.todo_android.Data.Todo.CreateTodo):
            com.lotus.todo_android.common.APIResponse<CreateTodoResponse>

    suspend fun readTodo(token: String, year: Int, month: Int, day: Int):
            com.lotus.todo_android.common.APIResponse<ReadTodoResponse>

    suspend fun updateTodo(token: String, id: String, updateTodo: com.lotus.todo_android.Data.Todo.UpdateTodo):
            com.lotus.todo_android.common.APIResponse<UpdateTodoResponse>

    suspend fun deleteTodo(token: String, id: String):
            com.lotus.todo_android.common.APIResponse<DeleteTodoResponse>
}