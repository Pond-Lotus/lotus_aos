package com.example.todo_android.repository.Todo

import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.common.APIResponse
import com.example.todo_android.dataresource.Todo.TodoRemoteDataResource
import com.example.todo_android.response.TodoResponse.CreateTodoResponse
import com.example.todo_android.response.TodoResponse.DeleteTodoResponse
import com.example.todo_android.response.TodoResponse.ReadTodoResponse
import com.example.todo_android.response.TodoResponse.UpdateTodoResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepositoryImpl @Inject constructor (
    private val TodoRemoteDataResource: TodoRemoteDataResource
) : TodoRepository {

    override suspend fun createTodo(
        token: String,
        createTodo: CreateTodo
    ): APIResponse<CreateTodoResponse> {

        // 회원가입 요청이 성공하면 Success에 데이터를 실어서 ,실패하면 Error에 message 리턴
        val response = TodoRemoteDataResource.createTodo(token, createTodo)
        if (response.execute().isSuccessful) {
            response.execute().body()?.let { result ->
                return APIResponse.Success(result)
            }
        }
        return APIResponse.Error(response.execute().message())
    }

    override suspend fun readTodo(
        token: String,
        year: Int,
        month: Int,
        day: Int
    ): APIResponse<ReadTodoResponse> {

        // 회원가입 요청이 성공하면 Success에 데이터를 실어서 ,실패하면 Error에 message 리턴
        val response = TodoRemoteDataResource.readTodo(token, year, month, day)
        if (response.execute().isSuccessful) {
            response.execute().body()?.let { result ->
                return APIResponse.Success(result)
            }
        }
        return APIResponse.Error(response.execute().message())
    }

    override suspend fun updateTodo(
        token: String,
        id: String,
        updateTodo: UpdateTodo
    ): APIResponse<UpdateTodoResponse> {

        // 회원가입 요청이 성공하면 Success에 데이터를 실어서 ,실패하면 Error에 message 리턴
        val response = TodoRemoteDataResource.updateTodo(token, id, updateTodo)
        if (response.execute().isSuccessful) {
            response.execute().body()?.let { result ->
                return APIResponse.Success(result)
            }
        }
        return APIResponse.Error(response.execute().message())
    }

    override suspend fun deleteTodo(token: String, id: String): APIResponse<DeleteTodoResponse> {

        // 회원가입 요청이 성공하면 Success에 데이터를 실어서 ,실패하면 Error에 message 리턴
        val response = TodoRemoteDataResource.deleteTodo(token, id)
        if (response.execute().isSuccessful) {
            response.execute().body()?.let { result ->
                return APIResponse.Success(result)
            }
        }
        return APIResponse.Error(response.execute().message())
    }
}