package com.example.todo_android.Data.Category

import java.util.Objects

data class UpdateCategory(
    val token: String,
    val priority: Map<String, String>
//    val priority: String
)