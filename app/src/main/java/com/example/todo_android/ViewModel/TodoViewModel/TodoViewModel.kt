package com.example.todo_android.ViewModel.TodoViewModel

import androidx.lifecycle.ViewModel
import com.example.todo_android.Data.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoViewModel : ViewModel() {

    // State Flow 초기값 설정
    private val _todoList = MutableStateFlow(emptyList<TodoItem>())

    val todoList = _todoList.asStateFlow()

    // ViewModel이 가지고 있는 값을 변경하는 메소드
    fun createTodoItem(text: String) {

    }

    fun readTodoItem(text: String) {

    }

    fun updateTodoItem(text: String) {

    }

    fun deleteTodoItem(text: String) {

    }
}