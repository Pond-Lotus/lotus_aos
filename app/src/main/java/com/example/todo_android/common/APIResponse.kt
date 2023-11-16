package com.example.todo_android.common

sealed class APIResponse<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T? = null): APIResponse<T>(data)
    class Loading<T>(data: T? = null): APIResponse<T>(data)
    class Error<T>(message: String, data: T? = null): APIResponse<T>(data, message)
}
