package com.example.todo_android.request.ProfileRequest

import com.example.todo_android.Data.Profile.AuthCode
import com.example.todo_android.response.ProfileResponse.AuthCodeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthCodeRequest {
    @POST("/account/emailcode/")
    fun requestCode(
        @Body authCodeRequest: AuthCode
    ) : Call<AuthCodeResponse>
}