package com.example.todo_android.response.ModifyResponse

import com.google.gson.annotations.SerializedName

data class DeleteProfileImageResponse(
    @SerializedName("resultCode")
    val resultCode: Int,
    @SerializedName("data")
    val data: DPRIesponse
)

// 응답값으로 data의 디테일한 값들
data class DPRIesponse(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("image")
    val image: String
)
