package com.example.todo_android.response.TodoResponse

import com.google.gson.annotations.SerializedName

data class CreateTodoResponse(
    @SerializedName("resultCode")
    val resultCode: Int,
    @SerializedName("data")
    val data: TodoData
)