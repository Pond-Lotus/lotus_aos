package com.example.todo_android.viewmodel.TodoViewModel

import androidx.lifecycle.ViewModel
import com.example.todo_android.common.APIResponse
import com.example.todo_android.repository.todo.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class TodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

//    State Flow 초기값 설정
//    private val _todoList = MutableStateFlow(emptyList<Todo>())
//
//    val todoList = _todoList.asStateFlow()

    private val _createTodoState = MutableStateFlow(null)
    private val _readTodoState = MutableStateFlow(null)
    private val _updateTodoState = MutableStateFlow(null)
    private val _deleteTodoState = MutableStateFlow(null)


    // ViewModel이 가지고 있는 값을 변경하는 메소드
    fun readTodo(text: String) {

    }

    fun createTodo(text: String) {

    }

    fun updateTodo(text: String) {

    }

    fun deleteTodo(text: String) {
    }


}

