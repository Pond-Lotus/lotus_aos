package com.lotus.todo_android.viewmodel.Todo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lotus.todo_android.Data.Todo.CreateTodo
import com.lotus.todo_android.Data.Todo.UpdateTodo
import com.lotus.todo_android.common.APIResponse
import com.lotus.todo_android.repository.Category.CategoryRepository
import com.lotus.todo_android.repository.Todo.TodoRepository
import com.lotus.todo_android.response.CategoryResponse.CategoryData
import com.lotus.todo_android.response.TodoResponse.TodoData
import com.lotus.todo_android.util.MyApplication
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

    private val _todoList = MutableStateFlow<List<TodoData>>(emptyList())
    private val _categoryList = MutableStateFlow(CategoryData())
    private val _categoryTodoList = MutableStateFlow<Map<Int?, List<TodoData>>>(emptyMap())

    private val _bottomsheetViewData = MutableStateFlow(TodoData())

    private val _todoBottomSheetColor = MutableStateFlow(0)

    val todoYear = _todoYear.asStateFlow()
    val todoMonth = _todoMonth.asStateFlow()
    val todoDay = _todoDay.asStateFlow()
    val todoTitle = _todoTitle.asStateFlow()
    val todoDescription = _todoDescription.asStateFlow()
    val todoColor = _todoColor.asStateFlow()
    val todoDone = _todoDone.asStateFlow()
    val todoTime = _todoTime.asStateFlow()

    val todoList = _todoList.asStateFlow()
    val categoryList = _categoryList.asStateFlow()
    val categoryTodoList = _categoryTodoList.asStateFlow()

    val bottomsheetViewData = _bottomsheetViewData.asStateFlow()

    val todoBottomSheetColor = _todoBottomSheetColor.asStateFlow()
    val todoBottomSheetTitle = MutableStateFlow("")
    val todoBottomSheetDescription = MutableStateFlow("")
    val todoBottomSheetTime = MutableStateFlow("9999")

    fun setTodoYear(year: Int) {
        _todoYear.value = year
    }

    fun setTodoMonth(month: Int) {
        _todoMonth.value = month
    }

    fun setTodoDay(day: Int) {
        _todoDay.value = day
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

    fun setTodoTime(time: String) {
        _todoTime.value = time
    }

    init {
        readCategory(token)
    }

    fun createTodo(token: String, createTodo: com.lotus.todo_android.Data.Todo.CreateTodo) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = TodoRepository.createTodo(token, createTodo)
            when (value) {
                is com.lotus.todo_android.common.APIResponse.Success -> {
                    val todoState = _todoList.value
                    val items = todoState.toMutableList().apply {
                        add(value.data!!.data)
                    }.toList()
                    _todoList.emit(items)
                    setTodoGroup()
                }
                is com.lotus.todo_android.common.APIResponse.Error -> {

                }
                is com.lotus.todo_android.common.APIResponse.Loading -> {

                }
            }
        }
    }

    fun readTodo(token: String, todoYear: Int, todoMonth: Int, todoDay: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = TodoRepository.readTodo(token, todoYear, todoMonth, todoDay)
            when (value) {
                is com.lotus.todo_android.common.APIResponse.Success -> {
                    _todoList.emit(value.data!!.data)
                    setTodoGroup()
                }
                is com.lotus.todo_android.common.APIResponse.Error -> {

                }
                is com.lotus.todo_android.common.APIResponse.Loading -> {

                }
            }
        }
    }


    fun updateTodo(token: String, id: String, updateTodo: com.lotus.todo_android.Data.Todo.UpdateTodo) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = TodoRepository.updateTodo(token, id, updateTodo)
            when (value) {
                is com.lotus.todo_android.common.APIResponse.Success -> {
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
                is com.lotus.todo_android.common.APIResponse.Error -> {

                }
                is com.lotus.todo_android.common.APIResponse.Loading -> {

                }
            }
        }
    }

    fun deleteTodo(token: String, todo: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = TodoRepository.deleteTodo(token, todo.id!!)
            when (value) {
                is com.lotus.todo_android.common.APIResponse.Success -> {
                    val todoState = _todoList.value
                    val items = todoState.toMutableList().apply {
                        remove(todo)
                    }.toList()
                    _todoList.value = items
                    setTodoGroup()
                }
                is com.lotus.todo_android.common.APIResponse.Error -> {

                }
                is com.lotus.todo_android.common.APIResponse.Loading -> {

                }
            }
        }
    }

    fun readCategory(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = CategoryRepository.readTodoCategory(token)
            when (value) {
                is com.lotus.todo_android.common.APIResponse.Success -> {
                    val categoryState = value.data!!.data
                    _categoryList.emit(categoryState)
                }
                is com.lotus.todo_android.common.APIResponse.Error -> {

                }
                is com.lotus.todo_android.common.APIResponse.Loading -> {

                }
            }
        }
    }

    fun updateCategory(token: String, data: Map<Int, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = CategoryRepository.updateTodoCategory(token, data)
            when (value) {
                is com.lotus.todo_android.common.APIResponse.Success -> {
                    val categoryKeys = data.map { it.key }.first()
                    val categoryData = data.map { it.value }.first()
                    when (categoryKeys) {
                        1 -> {
                            val categoryItem = _categoryList.value.copy(_1 = categoryData)
                            _categoryList.value = categoryItem
                            Log.d("categoryTest", "${_categoryList.value}")
                        }
                        2 -> {
                            val categoryItem = _categoryList.value.copy(_2 = categoryData)
                            _categoryList.value = categoryItem
                            Log.d("categoryTest", "${_categoryList.value}")
                        }
                        3 -> {
                            val categoryItem = _categoryList.value.copy(_3 = categoryData)
                            _categoryList.value = categoryItem
                            Log.d("categoryTest", "${_categoryList.value}")
                        }
                        4 -> {
                            val categoryItem = _categoryList.value.copy(_4 = categoryData)
                            _categoryList.value = categoryItem
                            Log.d("categoryTest", "${_categoryList.value}")
                        }
                        5 -> {
                            val categoryItem = _categoryList.value.copy(_5 = categoryData)
                            _categoryList.value = categoryItem
                            Log.d("categoryTest", "${_categoryList.value}")
                        }
                        6 -> {
                            val categoryItem = _categoryList.value.copy(_6 = categoryData)
                            _categoryList.value = categoryItem
                            Log.d("categoryTest", "${_categoryList.value}")
                        }
                    }
                }
                is com.lotus.todo_android.common.APIResponse.Error -> {

                }
                is com.lotus.todo_android.common.APIResponse.Loading -> {

                }
            }
        }
    }

    fun setBottomSheetDataSet(todo: TodoData) {
        _bottomsheetViewData.value = todo
        todoBottomSheetTitle.value = todo.title!!
        todoBottomSheetDescription.value = todo.description!!
        todoBottomSheetTime.value = todo.time!!
    }

    fun setTodoBottomSheetColor(color: Int) {
        _todoBottomSheetColor.value = color
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
        val categoryColor =
            todoList.value
                .sortedBy { it.color }
                .groupBy { it.color }
        viewModelScope.launch(Dispatchers.IO) {
            val sortData = categoryColor.mapValues { todo ->
                todo.value.sortedBy { it.done }
            }
            _categoryTodoList.emit(sortData)
        }
    }
}