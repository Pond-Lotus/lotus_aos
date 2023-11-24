package com.example.todo_android.viewmodel.Todo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.common.APIResponse
import com.example.todo_android.repository.Category.CategoryRepository
import com.example.todo_android.repository.Todo.TodoRepository
import com.example.todo_android.response.CategoryResponse.CategoryData
import com.example.todo_android.response.CategoryResponse.ReadCategoryResponse
import com.example.todo_android.response.TodoResponse.*
import com.example.todo_android.util.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository1: TodoRepository,
    private val repository2: CategoryRepository
) : ViewModel() {

    //    State Flow 초기값 설정
    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    private val _todoYear = MutableStateFlow(LocalDate.now().year)
    private val _todoMonth = MutableStateFlow(LocalDate.now().monthValue)
    private val _todoDay = MutableStateFlow(LocalDate.now().dayOfMonth)
    private val _todoTitle = MutableStateFlow("")
    private val _todoColor = MutableStateFlow(0)
    private val _todoList = MutableStateFlow<List<TodoData>>(emptyList())
    private val _categoryList = MutableStateFlow<List<CategoryData>>(emptyList())
    private val _categoryTodoList = MutableStateFlow<Map<Int?, List<TodoData>>>(emptyMap())

    val todoYear = _todoYear.asStateFlow()
    val todoMonth = _todoMonth.asStateFlow()
    val todoDay = _todoDay.asStateFlow()
    val todoTitle = _todoTitle.asStateFlow()
    val todoColor = _todoColor.asStateFlow()
    val todoList = _todoList.asStateFlow()
    val categoryList = _categoryList.asStateFlow()
    val categoryTodoList = _categoryTodoList.asStateFlow()

    init {
        readCategory(token)
    }



    fun createTodo(token: String, createTodo: CreateTodo) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = repository1.createTodo(token, createTodo)
            when (value) {
                is APIResponse.Success -> {
                    val todoState = _todoList.value
                    val items = todoState.toMutableList().apply {
                        add(value.data!!.data)
                    }.toList()
                    _todoList.value = items
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
            val value = repository1.readTodo(token, todoYear, todoMonth, todoDay)
            when (value) {
                is APIResponse.Success -> {
                    _todoList.emit(value.data!!.data)
                    setTodoGroup()
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
            val value = repository1.updateTodo(token, id, updateTodo)
            when (value) {
                is APIResponse.Success -> {
                    _todoList.emit(value.data!!.data)
                }
                is APIResponse.Error -> {

                }
                is APIResponse.Loading -> {

                }
            }
        }
    }

    fun deleteTodo(token: String, todo: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = repository1.deleteTodo(token, todo.id!!)
            when (value) {
                is APIResponse.Success -> {
                    val todoState = _todoList.value
                    val items = todoState.toMutableList().apply {
                        remove(todo)
                    }.toList()
                    _todoList.value = items
                }
                is APIResponse.Error -> {

                }
                is APIResponse.Loading -> {

                }
            }
        }
    }

    fun readCategory(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = repository2.readTodoCategory(token)
            when (value) {
                is APIResponse.Success -> {
                    val categoryState = _categoryList.value
                    val items = categoryState.toMutableList().apply {
                        add(value.data!!.data)
                    }.toList()
                    _categoryList.value = items
                }
                is APIResponse.Error -> {

                }
                is APIResponse.Loading -> {

                }
            }
        }
    }

    fun updateCategory(token: String, data: CategoryData) {
        viewModelScope.launch(Dispatchers.IO) {
//            val value = repository.updateTodoCategory(token, data)
//            when(value){
//                is APIResponse.Success -> {
//                    val categoryState = _categoryList.value
//                    val items = categoryState.toMutableList().toList()
//                    _categoryList.value = items
//                }
//                is APIResponse.Error -> {
//
//                }
//                is APIResponse.Loading -> {
//
//                }
//            }
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

    fun setTodoGroup(){
        val categoryColor = todoList.value.groupBy { it.color }
        viewModelScope.launch {
            _categoryTodoList.emit(categoryColor)
        }
    }
}

