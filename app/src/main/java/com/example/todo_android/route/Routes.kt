package com.example.todo_android.route

sealed class Routes(val route: String) {
    object Main : Routes("MainScreen")
}