package com.lotus.todo_android.Data.Todo

data class UpdateTodo(
    val year: Int,
    val month: Int,
    val day: Int,
    val title: String,
    val done: Boolean,
    val description: String,
    val color: Int,
    val time: String
)