package com.example.todo_android.Request.ProfileRequest

import com.example.todo_android.Data.Profile.Register
import com.example.todo_android.Response.ProfileResponse.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterRequest {
    @POST("/account/register/")
    fun requestRegister(
        @Body registerRequest: Register
    ) : Call<RegisterResponse>
}