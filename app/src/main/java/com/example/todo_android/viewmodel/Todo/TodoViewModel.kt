package com.example.todo_android.viewmodel.Todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.repository.todo.TodoRepository
import com.example.todo_android.response.TodoResponse.ReadTodoResponse
import com.example.todo_android.util.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

//    State Flow 초기값 설정
    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    private val _todoYear = MutableStateFlow(LocalDate.now().year)
    private val _todoMonth = MutableStateFlow(LocalDate.now().monthValue)
    private val _todoDay = MutableStateFlow(LocalDate.now().dayOfMonth)
    private val _todoTitle = MutableStateFlow("")
    private val _todoColor = MutableStateFlow("")

    private val _todoList = MutableStateFlow<List<ReadTodoResponse>>(emptyList())

    val todoYear = _todoYear.asStateFlow()
    val todoMonth = _todoMonth.asStateFlow()
    val todoDay = _todoDay.asStateFlow()
    val todoTitle = _todoTitle.asStateFlow()
    val todoColor = _todoColor.asStateFlow()

    val todoList = _todoList.asStateFlow()


    // ViewModel이 가지고 있는 값을 변경하는 메소드
    fun readTodo(token: String, todoYear: Int, todoMonth: Int, todoDay: Int) =
        viewModelScope.launch {
            repository.readTodo(token, todoYear, todoMonth, todoDay)
        }

    fun createTodo(token: String, createTodo: CreateTodo) = viewModelScope.launch {
        repository.createTodo(token, createTodo)
    }

    fun updateTodo(token: String, id: String, updateTodo: UpdateTodo) = viewModelScope.launch {
        repository.updateTodo(token, id, updateTodo)
    }

    fun deleteTodo(token: String, id: String) = viewModelScope.launch {
        repository.deleteTodo(token, id)
    }

    // todoColor 변경
    fun setTodoColor(color: String) {
        _todoColor.value = color
    }

    // todoTitle 변경
    fun inputTodoTitle(text: String){
        _todoTitle.value = text
    }
}

