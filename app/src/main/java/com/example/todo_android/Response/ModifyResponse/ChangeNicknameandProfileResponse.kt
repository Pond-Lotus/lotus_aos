package com.example.todo_android.Response.ModifyResponse

import com.example.todo_android.Response.TodoResponse.CTodoResponse
import com.google.gson.annotations.SerializedName

data class ChangeNicknameandProfileResponse(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("data")
    val data: CNAPResponse
)

// 응답값으로 data의 디테일한 값들
data class CNAPResponse(
    val nickname: String,
    val image: String
)
