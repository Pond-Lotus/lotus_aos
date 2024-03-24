package com.lotus.todo_android.dataresource.Todo

import com.lotus.todo_android.Data.Todo.CreateTodo
import com.lotus.todo_android.Data.Todo.UpdateTodo
import com.lotus.todo_android.response.TodoResponse.CreateTodoResponse
import com.lotus.todo_android.response.TodoResponse.DeleteTodoResponse
import com.lotus.todo_android.response.TodoResponse.ReadTodoResponse
import com.lotus.todo_android.response.TodoResponse.UpdateTodoResponse
import com.lotus.todo_android.service.Todo.TodoService
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRemoteDataResourceImpl @Inject constructor(
    private val todoService: TodoService
): TodoRemoteDataResource {

    override suspend fun createTodo(token: String, createTodo: com.lotus.todo_android.Data.Todo.CreateTodo): Call<CreateTodoResponse> {
        return todoService.requestCreateTodo(token, createTodo)
    }

    override suspend fun readTodo(token: String, year: Int, month: Int, day: Int): Call<ReadTodoResponse> {
        return todoService.requestReadTodo(token, year, month, day)
    }

    override suspend fun updateTodo(token: String, id: String, updateTodo: com.lotus.todo_android.Data.Todo.UpdateTodo): Call<UpdateTodoResponse> {
        return todoService.requestUpdateTodo(token, id, updateTodo)
    }

    override suspend fun deleteTodo(token: String, id: String): Call<DeleteTodoResponse> {
        return todoService.requestDeleteTodo(token, id)
    }
}