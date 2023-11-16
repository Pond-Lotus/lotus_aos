package com.example.todo_android.response.ModifyResponse

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("resultCode")
    val resultCode: String
)
