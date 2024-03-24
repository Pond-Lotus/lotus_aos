package com.lotus.todo_android.response.ProfileResponse

import com.google.gson.annotations.SerializedName

data class DeleteAccountResponse(
    @SerializedName("resultCode")
    val resultCode: Int
)
