package com.lotus.todo_android.response.CategoryResponse

import com.google.gson.annotations.SerializedName

data class ReadCategoryResponse(
    @SerializedName("resultCode")
    val resultCode: Int,
    @SerializedName("data")
    val data: CategoryData
)