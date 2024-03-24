package com.lotus.todo_android.response.TodoResponse

import com.google.gson.annotations.SerializedName

data class DeleteTodoResponse(
    @SerializedName("resultCode")
    val resultCode: Int
)

