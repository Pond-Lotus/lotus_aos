package com.example.todo_android.response.TodoResponse

import com.google.gson.annotations.SerializedName

data class DeleteTodoResponse(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("data")
    val data: DTodoResponse
)


// 응답값으로 data의 디테일한 값들
data class DTodoResponse(
    val title: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val writer: String,
)

