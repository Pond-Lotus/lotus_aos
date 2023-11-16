package com.example.todo_android.response.ProfileResponse

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("image")
    val image: String
)