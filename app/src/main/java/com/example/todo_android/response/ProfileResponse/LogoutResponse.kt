package com.example.todo_android.response.ProfileResponse

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("resultCode")
    val resultCode: Int
)
