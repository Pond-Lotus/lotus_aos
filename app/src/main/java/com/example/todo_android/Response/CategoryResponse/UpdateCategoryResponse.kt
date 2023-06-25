package com.example.todo_android.Response.CategoryResponse

import com.google.gson.annotations.SerializedName

data class UpdateCategoryResponse(
    @SerializedName("resultCode")
    val resultCode: Int
)
