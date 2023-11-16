package com.example.todo_android.request.ProfileRequest

import com.example.todo_android.Data.Profile.Login
import com.example.todo_android.response.ProfileResponse.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRequest {
    @POST("/account/login/")
    fun requestLogin(
        @Body loginRequest: Login
    ) : Call<LoginResponse>
}