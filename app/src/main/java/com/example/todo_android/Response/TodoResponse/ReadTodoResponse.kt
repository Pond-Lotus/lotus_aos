package com.example.todo_android.Response.TodoResponse

import com.google.gson.annotations.SerializedName

data class ReadTodoResponse(
    @SerializedName("resultCode")
    val resultCode: Int,
    @SerializedName("data")
    val data: ArrayList<RToDoResponse>
)


// 응답값으로 data의 디테일한 값들
data class RToDoResponse(
    val id: Int,
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
