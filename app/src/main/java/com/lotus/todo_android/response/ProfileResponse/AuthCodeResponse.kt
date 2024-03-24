package com.lotus.todo_android.response.ProfileResponse

import com.google.gson.annotations.SerializedName

data class AuthCodeResponse(
    @SerializedName("resultCode")
    val resultCode: String
)
