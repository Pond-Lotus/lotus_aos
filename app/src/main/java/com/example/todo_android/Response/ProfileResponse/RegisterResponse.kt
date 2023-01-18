package com.example.todo_android.Response.ProfileResponse

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("resultCode")
    val resultCode: String,
)