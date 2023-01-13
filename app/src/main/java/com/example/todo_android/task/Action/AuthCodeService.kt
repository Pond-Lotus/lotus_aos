package com.example.todo_android.task.Action

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface AuthCodeService {
    @FormUrlEncoded
    @POST("/account/emailcode/")
    fun requestCode(
        @Field("email") email: String,
        @Field("code") code: String
    ) : Call<AuthCode>
}