package com.lotus.todo_android.repository.Todo

import com.lotus.todo_android.Data.Todo.CreateTodo
import com.lotus.todo_android.Data.Todo.UpdateTodo
import com.lotus.todo_android.common.APIResponse
import com.lotus.todo_android.dataresource.Todo.TodoRemoteDataResource
import com.lotus.todo_android.response.TodoResponse.CreateTodoResponse
import com.lotus.todo_android.response.TodoResponse.DeleteTodoResponse
import com.lotus.todo_android.response.TodoResponse.ReadTodoResponse
import com.lotus.todo_android.response.TodoResponse.UpdateTodoResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepositoryImpl @Inject constructor(
    private val TodoRemoteDataResource: TodoRemoteDataResource
) : TodoRepository {

    override suspend fun createTodo(
        token: String,
        createTodo: com.lotus.todo_android.Data.Todo.CreateTodo
    ): com.lotus.todo_android.common.APIResponse<CreateTodoResponse> {

        // todo 생성 요청이 성공하면 Success에 데이터를 실어서 ,실패하면 Error에 message 리턴
        val response = TodoRemoteDataResource.createTodo(token, createTodo).clone().execute()
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return com.lotus.todo_android.common.APIResponse.Success(result)
            }
        }
        return com.lotus.todo_android.common.APIResponse.Error(response.message())
    }

    override suspend fun readTodo(
        token: String,
        year: Int,
        month: Int,
        day: Int
    ): com.lotus.todo_android.common.APIResponse<ReadTodoResponse> {

        // todo 조회 요청이 성공하면 Success에 데이터를 실어서 ,실패하면 Error에 message 리턴
        val response = TodoRemoteDataResource.readTodo(token, year, month, day).clone().execute()
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return com.lotus.todo_android.common.APIResponse.Success(result)
            }
        }
        return com.lotus.todo_android.common.APIResponse.Error(response.message())
    }

    override suspend fun updateTodo(
        token: String,
        id: String,
        updateTodo: com.lotus.todo_android.Data.Todo.UpdateTodo
    ): com.lotus.todo_android.common.APIResponse<UpdateTodoResponse> {

        // todo 수정 요청이 성공하면 Success에 데이터를 실어서 ,실패하면 Error에 message 리턴
        val response = TodoRemoteDataResource.updateTodo(token, id, updateTodo).clone().execute()
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return com.lotus.todo_android.common.APIResponse.Success(result)
            }
        }
        return com.lotus.todo_android.common.APIResponse.Error(response.message())
    }

    override suspend fun deleteTodo(token: String, id: String): com.lotus.todo_android.common.APIResponse<DeleteTodoResponse> {

        // todo 삭제 요청이 성공하면 Success에 데이터를 실어서 ,실패하면 Error에 message 리턴
        val response = TodoRemoteDataResource.deleteTodo(token, id).clone().execute()
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return com.lotus.todo_android.common.APIResponse.Success(result)
            }
        }
        return com.lotus.todo_android.common.APIResponse.Error(response.message())
    }
}