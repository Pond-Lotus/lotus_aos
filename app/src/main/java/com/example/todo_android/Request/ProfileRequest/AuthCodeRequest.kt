package com.example.todo_android.Request.ProfileRequest

import com.example.todo_android.Data.Profile.AuthCode
import com.example.todo_android.Response.ProfileResponse.AuthCodeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthCodeRequest {
    @POST("/account/emailcode/")
    fun requestCode(
        @Body authCodeRequest: AuthCode
    ) : Call<AuthCodeResponse>
}