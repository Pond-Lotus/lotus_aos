package com.example.todo_android.Request.ModifyRequest

import com.example.todo_android.Response.ModifyResponse.ChangePasswordResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ChangePasswordRequest {
    @FormUrlEncoded
    @POST("/account/edit2/")
    fun requestChangePassword(
        @Header("Authorization") token: String,
        @Field("password") password: String
    ): Call<ChangePasswordResponse>
}