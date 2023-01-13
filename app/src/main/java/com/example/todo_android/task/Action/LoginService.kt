package com.example.todo_android.task.Action

import com.example.todo_android.task.Login
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {
    @FormUrlEncoded
    @POST("/account/login/")
    fun requestLogin(
        @Field("email") userid: String,
        @Field("password") userpw: String
    ) : Call<Login>
}