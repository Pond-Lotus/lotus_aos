package com.example.todo_android.task.Action

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegisterService {
    @FormUrlEncoded
    @POST("/account/register/")
    fun requestRegister(
        @Field("email") userid: String,
        @Field("nickname") username: String,
        @Field("password") userpw: String
    ) : Call<Register>
}