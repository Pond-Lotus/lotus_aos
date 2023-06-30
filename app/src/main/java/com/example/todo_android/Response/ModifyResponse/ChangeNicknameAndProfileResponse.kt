package com.example.todo_android.Response.ModifyResponse

import com.google.gson.annotations.SerializedName

data class ChangeNicknameAndProfileResponse(
    @SerializedName("resultCode")
    val resultCode: Int,
    @SerializedName("data")
    val data: CNAPResponse
)

// 응답값으로 data의 디테일한 값들
data class CNAPResponse(
    val nickname: String,
    val image: String
)
