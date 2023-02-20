package com.example.todo_android.Request.ModifyRequest

import com.example.todo_android.Data.Modify.ChangePassword
import com.example.todo_android.Response.ModifyResponse.ChangePasswordResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ChangePasswordRequest {
    @POST("/account/edit2/")
    fun requestChangePassword(
        @Header("Authorization") token: String,
        @Body changePasswordRequest: ChangePassword
    ): Call<ChangePasswordResponse>
}