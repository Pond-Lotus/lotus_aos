package com.example.todo_android.Response

import com.google.gson.annotations.SerializedName

data class AuthEmailResponse(
    @SerializedName("resultCode")
    val resultCode: String,
)
