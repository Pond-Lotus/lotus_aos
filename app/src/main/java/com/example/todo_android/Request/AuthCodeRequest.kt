package com.example.todo_android.Request

import com.example.todo_android.Data.AuthCode
import com.example.todo_android.Response.AuthCodeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthCodeRequest {
    @POST("/account/emailcode/")
    fun requestCode(
        @Body authCodeRequest: AuthCode
    ) : Call<AuthCodeResponse>
}