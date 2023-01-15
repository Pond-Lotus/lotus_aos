package com.example.todo_android.task.Action

import com.example.todo_android.task.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginRequest {
    @FormUrlEncoded
    @POST("/account/login/")
    fun requestLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<LoginResponse>
}