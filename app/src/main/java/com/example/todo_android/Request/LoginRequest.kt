package com.example.todo_android.Request

import com.example.todo_android.Data.Login
import com.example.todo_android.Response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRequest {
//    @FormUrlEncoded
    @POST("/account/login/")
    fun requestLogin(
        @Body loginRequest: Login
    ) : Call<LoginResponse>
//    fun requestLogin(
//        @Field("email") email: String,
//        @Field("password") password: String
//    ) : Call<LoginResponse>
}