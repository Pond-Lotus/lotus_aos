package com.example.todo_android.response.CategoryResponse

import com.google.gson.annotations.SerializedName

data class UpdateCategoryResponse(
    @SerializedName("resultCode")
    val resultCode: Int
)
