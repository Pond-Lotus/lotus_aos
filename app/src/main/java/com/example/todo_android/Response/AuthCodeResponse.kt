package com.example.todo_android.Response

import com.google.gson.annotations.SerializedName

data class AuthCodeResponse(
    @SerializedName("resultCode")
    val resultCode: String
)
