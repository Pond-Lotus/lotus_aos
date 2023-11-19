package com.example.todo_android.dataresource.todo

import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.request.TodoRequest.CreateTodoRequest
import com.example.todo_android.request.TodoRequest.DeleteTodoRequest
import com.example.todo_android.request.TodoRequest.ReadTodoRequest
import com.example.todo_android.request.TodoRequest.UpdateTodoRequest
import com.example.todo_android.response.TodoResponse.CreateTodoResponse
import com.example.todo_android.response.TodoResponse.DeleteTodoResponse
import com.example.todo_android.response.TodoResponse.ReadTodoResponse
import com.example.todo_android.response.TodoResponse.UpdateTodoResponse
import retrofit2.Call

class TodoRemoteDataResourceImpl(
    private val createTodoService: CreateTodoRequest,
    private val readTodoService: ReadTodoRequest,
    private val updateTodoService: UpdateTodoRequest,
    private val deleteTodoService: DeleteTodoRequest
): TodoRemoteDataResource {

    override suspend fun createTodo(token: String, createTodo: CreateTodo): Call<CreateTodoResponse> {
        return createTodoService.requestCreateTodo(token, createTodo)
    }

    override suspend fun readTodo(token: String, year: Int, month: Int, day: Int): Call<ReadTodoResponse> {
        return readTodoService.requestReadTodo(token, year, month, day)
    }

    override suspend fun updateTodo(token: String, id: String, updateTodo: UpdateTodo): Call<UpdateTodoResponse> {
        return updateTodoService.requestUpdateTodo(token, id, updateTodo)
    }

    override suspend fun deleteTodo(token: String, id: String): Call<DeleteTodoResponse> {
        return deleteTodoService.requestDeleteTodo(token, id)
    }
}