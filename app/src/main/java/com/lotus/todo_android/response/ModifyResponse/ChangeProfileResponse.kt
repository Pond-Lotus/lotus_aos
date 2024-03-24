package com.lotus.todo_android.response.ModifyResponse

import com.google.gson.annotations.SerializedName

data class ChangeProfileResponse(
    @SerializedName("resultCode")
    val resultCode: Int,
    @SerializedName("data")
    val data: CPResponse
)

// 응답값으로 data의 디테일한 값들
data class CPResponse(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("image")
    val image: String
)
