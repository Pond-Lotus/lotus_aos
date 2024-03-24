package com.lotus.todo_android.response.ProfileResponse

import com.google.gson.annotations.SerializedName

data class AuthEmailResponse(
    @SerializedName("resultCode")
    val resultCode: String,
)
