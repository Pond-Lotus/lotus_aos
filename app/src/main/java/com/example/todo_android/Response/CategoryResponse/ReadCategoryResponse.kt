package com.example.todo_android.Response.CategoryResponse

import com.google.gson.annotations.SerializedName

data class ReadCategoryResponse(
    @SerializedName("resultCode")
    val resultCode: Int,
    @SerializedName("data")
    val data: Map<String, String>
//    val data: ArrayList<RCategoryResponse>
)

//// 응답값으로 data의 디테일한 값들
//data class RCategoryResponse(
//    val `1`: String,
//    val `2`: String,
//    val `3`: String,
//    val `4`: String,
//    val `5`: String,
//    val `6`: String
//    )