package com.example.todo_android.viewmodel.Todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.common.APIResponse
import com.example.todo_android.repository.Todo.TodoRepository
import com.example.todo_android.response.TodoResponse.*
import com.example.todo_android.util.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val _todoColor = MutableStateFlow(0)

    val todoYear = _todoYear.asStateFlow()

    val todoMonth = _todoMonth.asStateFlow()
    val todoDay = _todoDay.asStateFlow()
    val todoTitle = _todoTitle.asStateFlow()
    val todoColor = _todoColor.asStateFlow()


    private val _todoReadList = MutableStateFlow<List<RToDoResponse>>(emptyList())
    private val _todoCreateList = MutableStateFlow<List<CTodoResponse>>(emptyList())
    private val _todoUpdateList = MutableStateFlow<List<UTodoResponse>>(emptyList())
    private val _todoDeleteList = MutableStateFlow<List<DeleteTodoResponse>>(emptyList())

    val todoReadList = _todoReadList.asStateFlow()
    val todoCreateList = _todoCreateList.asStateFlow()
    val todoUpdateList = _todoUpdateList.asStateFlow()
    val todoDeleteList = _todoDeleteList.asStateFlow()


    fun createTodo(token: String, createTodo: CreateTodo) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = repository.createTodo(token, createTodo)
            when (value) {
                is APIResponse.Success -> {
                    _todoCreateList.value = listOf(value.data!!.data)
                }
                is APIResponse.Error -> {

                }
                is APIResponse.Loading -> {

                }
            }
        }
    }

    fun readTodo(token: String, todoYear: Int, todoMonth: Int, todoDay: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = repository.readTodo(token, todoYear, todoMonth, todoDay)
            when(value){
                is APIResponse.Success -> {
                    _todoReadList.value = value.data!!.data
                }
                is APIResponse.Error -> {

                }
                is APIResponse.Loading -> {

                }
            }
        }
    }


    fun updateTodo(token: String, id: String, updateTodo: UpdateTodo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(token, id, updateTodo)
        }
    }

    fun deleteTodo(token: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodo(token, id)
        }
    }


    // todoColor 변경
    fun setTodoColor(color: Int) {
        _todoColor.value = color
    }

    // todoTitle 변경
    fun setTodoTitle(text: String) {
        _todoTitle.value = text
    }
}

