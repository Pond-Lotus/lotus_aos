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
import com.example.todo_android.response.TodoResponse.TodoData
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
    private val TodoRepository: TodoRepository,
    private val CategoryRepository: CategoryRepository
) : ViewModel() {

    //    State Flow 초기값 설정
    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    private val _todoYear = MutableStateFlow(LocalDate.now().year)
    private val _todoMonth = MutableStateFlow(LocalDate.now().monthValue)
    private val _todoDay = MutableStateFlow(LocalDate.now().dayOfMonth)
    private val _todoTitle = MutableStateFlow("")
    private val _todoDescription = MutableStateFlow("")
    private val _todoColor = MutableStateFlow(0)
    private val _todoDone = MutableStateFlow(false)
    private val _todoTime = MutableStateFlow("9999")
    private val _todoAmPm = MutableStateFlow("")

    private val _todoList = MutableStateFlow<List<TodoData>>(emptyList())
    private val _categoryList = MutableStateFlow<List<CategoryData>>(emptyList())
    private val _categoryTodoList = MutableStateFlow<Map<Int?, List<TodoData>>>(emptyMap())

    private val _bottomsheetViewData = MutableStateFlow(TodoData())

    val todoYear = _todoYear.asStateFlow()
    val todoMonth = _todoMonth.asStateFlow()
    val todoDay = _todoDay.asStateFlow()
    val todoTitle = _todoTitle.asStateFlow()
    val todoDescription = _todoDescription.asStateFlow()
    val todoColor = _todoColor.asStateFlow()
    val todoDone = _todoDone.asStateFlow()
    val todoTime = _todoTime.asStateFlow()
    val todoAmPm = _todoAmPm.asStateFlow()

    val todoList = _todoList.asStateFlow()
    val categoryList = _categoryList.asStateFlow()
    val categoryTodoList = _categoryTodoList.asStateFlow()

    val bottomsheetViewData = _bottomsheetViewData.asStateFlow()

    init {
        readCategory(token)
    }


    fun createTodo(token: String, createTodo: CreateTodo) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = TodoRepository.createTodo(token, createTodo)
            when (value) {
                is APIResponse.Success -> {
                    val todoState = _todoList.value
                    val items = todoState.toMutableList().apply {
                        add(value.data!!.data)
                    }.toList()
                    _todoList.emit(items)
                    setTodoGroup()
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
            val value = TodoRepository.readTodo(token, todoYear, todoMonth, todoDay)
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
            val value = TodoRepository.updateTodo(token, id, updateTodo)
            when (value) {
                is APIResponse.Success -> {
                    Log.d("updateTodo", value.data.toString())

                    val todoState = _todoList.value
                    val todoItem = todoState.filter { it.id == id }.last()
                    val index = _todoList.value.toList().indexOf(todoItem)
                    val updateTodoData = todoItem.copy(
                        id = id,
                        year = updateTodo.year,
                        month = updateTodo.month,
                        day = updateTodo.day,
                        title = updateTodo.title,
                        description = updateTodo.description,
                        done = updateTodo.done,
                        time = updateTodo.time,
                        color = updateTodo.color
                    )
                    val updateTodoList = _todoList.value.toMutableList().apply {
                        set(index, updateTodoData)
                    }

                    viewModelScope.launch {
                        _todoList.emit(updateTodoList)
                        setTodoGroup()
                    }
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
            val value = TodoRepository.deleteTodo(token, todo.id!!)
            when (value) {
                is APIResponse.Success -> {
                    val todoState = _todoList.value
                    val items = todoState.toMutableList().apply {
                        remove(todo)
                    }.toList()
                    _todoList.value = items
                    setTodoGroup()
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
            val value = CategoryRepository.readTodoCategory(token)
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

        }
    }


    fun setTodoColor(color: Int) {
        _todoColor.value = color
    }

    fun setTodoTitle(text: String) {
        _todoTitle.value = text
    }

    fun setTodoDescription(text: String) {
        _todoDescription.value = text
    }

    fun setBottomSheetDataSet(
        todo: TodoData
    ) {
        viewModelScope.launch {
            _bottomsheetViewData.emit(todo)
        }
    }

    fun setTodoDone(todo: TodoData, done: Boolean) {

        val todoState = _todoList.value
        val todoItem = todoState.filter { it.id == todo.id }.last()
        val index = _todoList.value.toList().indexOf(todoItem)
        val updateTodoData = todoItem.copy(done = done)
        val updateTodoList = _todoList.value.toMutableList().apply {
            set(index, updateTodoData)
        }

        viewModelScope.launch {
            // todo checkbox 클릭하는 부분
            _todoList.emit(updateTodoList)
            setTodoGroup()
        }
    }

    fun setTodoGroup() {
        val categoryColor = todoList.value.groupBy { it.color }
        viewModelScope.launch(Dispatchers.IO) {
            val sortData = categoryColor.mapValues { todo ->
                todo.value.sortedBy { it.done }
            }
            _categoryTodoList.emit(sortData)
        }
    }
}