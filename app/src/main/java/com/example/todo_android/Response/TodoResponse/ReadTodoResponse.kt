package com.example.todo_android.Response.TodoResponse

data class ReadTodoResponse(
    val resultCode: Int,
    val data: ArrayList<DataResponse>
)


// 응답값으로 data의 디테일한 값들
data class DataResponse(
    val id: Int,
    val title: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val done: Boolean,
    val writer: String
)
