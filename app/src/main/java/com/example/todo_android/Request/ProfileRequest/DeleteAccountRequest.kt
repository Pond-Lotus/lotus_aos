package com.example.todo_android.Request.ProfileRequest

import com.example.todo_android.Response.ProfileResponse.DeleteAccountResponse
import com.example.todo_android.Response.TodoResponse.DeleteTodoResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Header

interface DeleteAccountRequest {
    @DELETE("/account/withdrawal/")
    fun requestDeleteTodo(
        @Header("Authorization") token: String
    ): Call<DeleteAccountResponse>
}