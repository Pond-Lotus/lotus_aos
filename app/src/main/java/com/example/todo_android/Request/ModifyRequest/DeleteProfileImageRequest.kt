package com.example.todo_android.Request.ModifyRequest

import com.example.todo_android.Response.ModifyResponse.DeleteProfileImageResponse
import com.example.todo_android.Response.TodoResponse.DeleteTodoResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface DeleteProfileImageRequest {
    @POST("/account/edit1/")
    fun requestDeleteProfileImage(
        @Header("Authorization") token: String
    ): Call<DeleteProfileImageResponse>
}