package com.example.todo_android.Request.ProfileRequest

import com.example.todo_android.Data.Profile.Login
import com.example.todo_android.Response.ProfileResponse.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRequest {
    @POST("/account/login/")
    fun requestLogin(
//        @Header("Authorization") token: String,
        @Body loginRequest: Login
    ) : Call<LoginResponse>
}