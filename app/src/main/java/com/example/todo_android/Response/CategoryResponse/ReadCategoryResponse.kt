package com.example.todo_android.Response.CategoryResponse

import com.google.gson.annotations.SerializedName

data class ReadCategoryResponse(
    @SerializedName("resultCode")
    val resultCode: Int,
    @SerializedName("data")
    val data: Map<String, String>
)