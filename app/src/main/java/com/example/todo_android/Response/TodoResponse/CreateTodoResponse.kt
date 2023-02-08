package com.example.todo_android.Response.TodoResponse

import com.google.gson.annotations.SerializedName

data class CreateTodoResponse(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("data")
    val data: CTodoResponse,
)


// 응답값으로 data의 디테일한 값들
data class CTodoResponse(
    val title: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val writer: String,
    val done: Boolean,
    val color: Int,
    val time: String,
    val description: String,
    val id: String
)
