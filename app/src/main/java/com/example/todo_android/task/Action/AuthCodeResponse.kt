package com.example.todo_android.task.Action

import com.google.gson.annotations.SerializedName

data class AuthCodeResponse(
    @SerializedName("resultCode")
    val resultCode: String
)
