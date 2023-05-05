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
    val id: String,
    val writer: String,
    val year: String,
    val month: String,
    val day: String,
    val title: String,
    val description: String,
    val done: Boolean,
    val time: String,
    val color: Int,


)