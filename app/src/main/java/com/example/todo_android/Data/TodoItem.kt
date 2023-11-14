package com.example.todo_android.Data

data class TodoItem(
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
