package com.example.todo_android.Data.Todo

data class UpdateTodo(
    val year: Int,
    val month: Int,
    val day: Int,
    val title: String,
    val done: Int,
    val description: String,
    val color: Int,
    val time: String
)