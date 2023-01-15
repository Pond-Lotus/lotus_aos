package com.example.todo_android.task

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("token")
    val token: String
)