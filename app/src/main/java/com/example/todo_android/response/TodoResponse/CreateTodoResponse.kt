package com.example.todo_android.response.TodoResponse

import com.google.gson.annotations.SerializedName

data class CreateTodoResponse(
    @SerializedName("resultCode")
    val resultCode: Int,
    @SerializedName("data")
    val data: CTodoResponse
)


// 응답값으로 data의 디테일한 값들
data class CTodoResponse(
    val id: String,
    val writer: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val title: String,
    val description: String,
    val done: Boolean,
    val time: String,
    val color: Int
)