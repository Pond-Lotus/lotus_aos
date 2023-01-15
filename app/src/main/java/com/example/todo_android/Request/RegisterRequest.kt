package com.example.todo_android.Request

import com.example.todo_android.Data.Register
import com.example.todo_android.Response.RegisterReponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterRequest {
    @POST("/account/register/")
    fun requestRegister(
        @Body registerRequest: Register
    ) : Call<RegisterReponse>
}