package com.example.todo_android.Response.TodoResponse

import android.util.EventLogTags.Description
import com.google.gson.annotations.SerializedName

data class UpdateTodoResponse(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("data")
    val data: UTodoResponse
)

// 응답값으로 data의 디테일한 값들
data class UTodoResponse(
    val title: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val color: Int,
    val description: String,
    val time: String,
    val id: Int,
    val done: Boolean,
)
