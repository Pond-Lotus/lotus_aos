package com.example.todo_android.Response

import com.google.gson.annotations.SerializedName

data class RegisterReponse(
    @SerializedName("resultCode")
    val resultCode: String,
)