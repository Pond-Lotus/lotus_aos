package com.lotus.todo_android.response.TodoResponse

data class TodoData(
    val id: String? = null,
    val writer: String? = null,
    val year: Int? = null,
    val month: Int? = null,
    val day: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val done: Boolean? = null,
    val time: String? = null,
    val color: Int? = null
)
