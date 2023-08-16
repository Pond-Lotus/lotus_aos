package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.DismissDirection
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import com.example.todo_android.Component.FloatingStateType
import com.example.todo_android.Component.MonthWeekToggleSwitch
import com.example.todo_android.Component.ProfileModalDrawer
import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.R
import com.example.todo_android.Request.CategoryRequest.ReadCategoryRequest
import com.example.todo_android.Request.TodoRequest.CreateTodoRequest
import com.example.todo_android.Request.TodoRequest.DeleteTodoRequest
import com.example.todo_android.Request.TodoRequest.ReadTodoRequest
import com.example.todo_android.Request.TodoRequest.UpdateTodoRequest
import com.example.todo_android.Response.CategoryResponse.ReadCategoryResponse
import com.example.todo_android.Response.TodoResponse.*
import com.example.todo_android.Util.MyApplication
import com.example.todo_android.ui.theme.deleteBackground
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.color.KalendarThemeColor
import com.himanshoe.kalendar.component.day.config.KalendarDayColors
import com.himanshoe.kalendar.component.header.config.KalendarHeaderConfig
import com.himanshoe.kalendar.component.text.config.KalendarTextColor
import com.himanshoe.kalendar.component.text.config.KalendarTextConfig
import com.himanshoe.kalendar.component.text.config.KalendarTextSize
import com.himanshoe.kalendar.model.KalendarDay
import com.himanshoe.kalendar.model.KalendarEvent
import com.himanshoe.kalendar.model.KalendarType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Time
import java.time.LocalDate
import java.util.*

fun createTodo(
    token: String,
    year: String,
    month: String,
    day: String,
    title: String,
    color: String,
    response: (CreateTodoResponse?) -> Unit,
) {

    var createTodoResponse: CreateTodoResponse? = null

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit = Retrofit.Builder().baseUrl("http://34.22.73.14:8000/").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

    var createTodoRequest: CreateTodoRequest = retrofit.create(CreateTodoRequest::class.java)

    createTodoRequest.requestCreateTodo(token, CreateTodo(year, month, day, title, color))
        .enqueue(object : Callback<CreateTodoResponse> {

            // 실패 했을때
            override fun onFailure(call: Call<CreateTodoResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
            }

            // 성공 했을때
            override fun onResponse(
                call: Call<CreateTodoResponse>,
                response: Response<CreateTodoResponse>,
            ) {
                createTodoResponse = response.body()

                response(createTodoResponse)

                Log.d("createTodo", "token : " + MyApplication.prefs.getData("token", ""))
                Log.d("createTodo", "resultCode : " + createTodoResponse?.resultCode)
                Log.d("createTodo", "data : " + createTodoResponse?.data)

            }
        })
}

fun readTodo(
    token: String,
    year: String,
    month: String,
    day: String,
    response: (ReadTodoResponse?) -> Unit,
) {

    var readTodoResponse: ReadTodoResponse? = null

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit = Retrofit.Builder().baseUrl("http://34.22.73.14:8000/").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

    var readTodoRequest: ReadTodoRequest = retrofit.create(ReadTodoRequest::class.java)

    readTodoRequest.requestReadTodo(token, year, month, day)
        .enqueue(object : Callback<ReadTodoResponse> {

            //실패할 경우
            override fun onFailure(call: Call<ReadTodoResponse>, t: Throwable) {
                Log.e("readTodo", t.message.toString())
            }

            //성공할 경우
            override fun onResponse(
                call: Call<ReadTodoResponse>,
                response: Response<ReadTodoResponse>,
            ) {
                readTodoResponse = response.body()

                response(readTodoResponse)

                Log.d("readTodo", "token : " + MyApplication.prefs.getData("token", ""))
                Log.d("readTodo", "resultCode : " + readTodoResponse?.resultCode)
                Log.d("readTodo", "data : " + readTodoResponse?.data)
            }
        })
}

fun deleteTodo(
    token: String,
    id: String,
    response: (DeleteTodoResponse?) -> Unit,
) {
    var deleteTodoResponse: DeleteTodoResponse? = null

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit = Retrofit.Builder().baseUrl("http://34.22.73.14:8000/").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

    var deleteTodoRequest: DeleteTodoRequest = retrofit.create(DeleteTodoRequest::class.java)

    deleteTodoRequest.requestDeleteTodo(token, id).enqueue(object : Callback<DeleteTodoResponse> {

        // 실패 했을때
        override fun onFailure(call: Call<DeleteTodoResponse>, t: Throwable) {
            Log.e("updateTodo", t.message.toString())
        }

        // 성공 했을때
        override fun onResponse(
            call: Call<DeleteTodoResponse>,
            response: Response<DeleteTodoResponse>,
        ) {
            deleteTodoResponse = response.body()

            response(deleteTodoResponse)

            Log.d("deleteTodo", "token : " + MyApplication.prefs.getData("token", ""))
            Log.d("deleteTodo", "resultCode : " + deleteTodoResponse?.resultCode)
            Log.d("deleteTodo", "data : " + deleteTodoResponse?.data)
        }
    })
}

fun updateTodo(
    token: String,
    year: String,
    month: String,
    day: String,
    title: String,
    done: Boolean,
    description: String,
    color: String,
    time: String,
    id: String,
    response: (UpdateTodoResponse?) -> Unit,
) {

    var updateTodoResponse: UpdateTodoResponse? = null

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit = Retrofit.Builder().baseUrl("http://34.22.73.14:8000/").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

    var updateTodoRequest: UpdateTodoRequest = retrofit.create(UpdateTodoRequest::class.java)

    updateTodoRequest.requestUpdateTodo(
        token, id, UpdateTodo(year, month, day, title, done, description, color, time)
    ).enqueue(object : Callback<UpdateTodoResponse> {

        // 실패 했을때
        override fun onFailure(call: Call<UpdateTodoResponse>, t: Throwable) {
            Log.e("updateTodo", t.message.toString())
        }

        // 성공 했을때
        override fun onResponse(
            call: Call<UpdateTodoResponse>,
            response: Response<UpdateTodoResponse>,
        ) {

            if (response.isSuccessful) {
                updateTodoResponse = response.body()

                response(updateTodoResponse)

                Log.d("updateTodo", "token : " + MyApplication.prefs.getData("token", ""))
                Log.d("updateTodo", "resultCode : " + updateTodoResponse?.resultCode)
            } else {
                Log.e("updateTodo", "resultCode : " + response.body())
                Log.e("updateTodo", "code : " + response.code())
            }
        }
    })
}

fun readCategory(
    response: (ReadCategoryResponse?) -> Unit
) {
    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    var readCategoryResponse: ReadCategoryResponse? = null

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit = Retrofit.Builder().baseUrl("http://34.22.73.14:8000/").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

    var readCategoryRequest: ReadCategoryRequest = retrofit.create(ReadCategoryRequest::class.java)

    readCategoryRequest.requestReadCategory(token).enqueue(object : Callback<ReadCategoryResponse> {
        override fun onResponse(
            call: Call<ReadCategoryResponse>, response: Response<ReadCategoryResponse>
        ) {
            readCategoryResponse = response.body()
            response(readCategoryResponse)
            Log.d("readCategory", "resultCode : " + readCategoryResponse?.resultCode)
            Log.d("readCategory", "data : " + readCategoryResponse?.data)
        }

        override fun onFailure(call: Call<ReadCategoryResponse>, t: Throwable) {
            Log.e("readCategory", t.message.toString())
        }

    })
}

@ExperimentalMotionApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter", "NewApi"
)
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun CalendarScreen(routeAction: RouteAction) {

    val states = listOf("월간", "주간")
//    var selectedOption by remember { mutableStateOf(states[0]) }

//    val onSelectionChange = { text: String -> selectedOption = text }

//    var selectCalendar by remember { mutableStateOf(false) }
    var isVisibility by remember { mutableStateOf(false) }

    var multiFloatingState by remember { mutableStateOf(FloatingStateType.Collapsed) }

    val animateState = remember { mutableStateOf(false) }

    val colorFAB = if (multiFloatingState == FloatingStateType.Expanded) {
        Color(0xff9E9E9E)
    } else {
        Color(0xffFFDAB9)
    }


    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    var year by remember { mutableStateOf(LocalDate.now().year.toString()) }
    var month by remember { mutableStateOf(LocalDate.now().monthValue.toString()) }
    var day by remember { mutableStateOf(LocalDate.now().dayOfMonth.toString()) }
    var title by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }

    var dayString by remember {
        mutableStateOf("")
    }

    var todoList = remember { mutableStateListOf<RToDoResponse>() }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()
    val bottomScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))

    var selectedTodo by remember { mutableStateOf<RToDoResponse?>(null) }

    LaunchedEffect(isVisibility) {
        if (isVisibility) {
            focusRequester.requestFocus()
        }
    }

    val onButtonClick: (String) -> Unit = { id ->
        when (id) {
            "1" -> {
                isVisibility = !isVisibility
                color = "1"
                Log.d("id", "id : ${id}")
            }
            "2" -> {
                isVisibility = !isVisibility
                color = "2"
                Log.d("id", "id : ${id}")
            }
            "3" -> {
                isVisibility = !isVisibility
                color = "3"
                Log.d("id", "id : ${id}")
            }
            "4" -> {
                isVisibility = !isVisibility
                color = "4"
                Log.d("id", "id : ${id}")
            }
            "5" -> {
                isVisibility = !isVisibility
                color = "5"
                Log.d("id", "id : ${id}")
            }
            "6" -> {
                isVisibility = !isVisibility
                color = "6"
                Log.d("id", "id : ${id}")
            }
        }
    }

    LaunchedEffect(key1 = Unit, block = {

        day = LocalDate.now().dayOfMonth.toString()

        val selectedDate = LocalDate.of(
            LocalDate.now().year, LocalDate.now().monthValue, LocalDate.now().dayOfMonth
        )
        val dayOfWeek = selectedDate.dayOfWeek

        dayString = when (dayOfWeek.value) {
            1 -> "월요일"
            2 -> "화요일"
            3 -> "수요일"
            4 -> "목요일"
            5 -> "금요일"
            6 -> "토요일"
            7 -> "일요일"
            else -> ""
        }

        scope.launch {
            readTodo(
                token,
                LocalDate.now().year.toString(),
                LocalDate.now().monthValue.toString(),
                LocalDate.now().dayOfMonth.toString()
            ) {
                todoList.clear()
                for (i in it!!.data) {
                    todoList.add(i)
                }
            }
        }
    })
    BottomSheetScaffold(
        scaffoldState = bottomScaffoldState,
        drawerContent = {
            ProfileModalDrawer(
                scope = scope, bottomScaffoldState = bottomScaffoldState, routeAction = routeAction
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    MonthWeekToggleSwitch(
                        width = 115,
                        height = 35,
                        animateState = animateState
                    )
            }, navigationIcon = {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(start = 21.dp)
                        .clickable {
                            scope.launch {
                                bottomScaffoldState.drawerState.open()
                            }
                        },
                    painter = painterResource (id = R.drawable.menubar),
                    contentDescription = "menubar")
            }, colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.White, titleContentColor = Color.Black
            ))
        },
        floatingActionButton = {
            AddTodoFloatingButton(
                multiFloatingState = multiFloatingState, onMultiFloatingStateChange = {
                    multiFloatingState = it
                }, backgroundColor = colorFAB, onButtonClick = onButtonClick
            )
        },
        floatingActionButtonPosition = androidx.compose.material.FabPosition.End,
        sheetContent = {
            selectedTodo?.let { TodoUpdateBottomSheet(scope, bottomScaffoldState, it, todoList) }
        },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(
            topStart = 20.dp, topEnd = 20.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xfff0f0f0))
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .shadow(
                        shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp),
                        elevation = 3.dp
                    )
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300
                        )
                    )
            ) {
                if (animateState.value) {
                    Kalendar(modifier = Modifier.fillMaxWidth(),
                        kalendarHeaderConfig = KalendarHeaderConfig(
                            kalendarTextConfig = KalendarTextConfig(
                                kalendarTextColor = KalendarTextColor(Color.Black),
                                kalendarTextSize = KalendarTextSize.SubTitle
                            )
                        ),
                        kalendarType = KalendarType.Oceanic(),
                        kalendarDayColors = KalendarDayColors(Color.Black, Color.Black),
                        kalendarThemeColor = KalendarThemeColor(
                            backgroundColor = Color.White,
                            dayBackgroundColor = Color(0xffFBE3C7),
                            headerTextColor = Color.Black
                        ),
                        onCurrentDayClick = { kalendarDay: KalendarDay, kalendarEvents: List<KalendarEvent> ->

                            year = kalendarDay.localDate.year.toString()
                            month = kalendarDay.localDate.monthNumber.toString()
                            day = kalendarDay.localDate.dayOfMonth.toString()

                            val selectedDate = LocalDate.of(
                                kalendarDay.localDate.year,
                                kalendarDay.localDate.monthNumber,
                                kalendarDay.localDate.dayOfMonth
                            )
                            val dayOfWeek = selectedDate.dayOfWeek

                            dayString = when (dayOfWeek.value) {
                                1 -> "월요일"
                                2 -> "화요일"
                                3 -> "수요일"
                                4 -> "목요일"
                                5 -> "금요일"
                                6 -> "토요일"
                                7 -> "일요일"
                                else -> ""
                            }
                            scope.launch {
                                readTodo(token, year, month, day) {
                                    todoList.clear()
                                    for (i in it!!.data) {
                                        todoList.add(i)
                                    }
                                }
                            }
                        })
                } else {
                    Kalendar(modifier = Modifier.fillMaxWidth(),
                        kalendarHeaderConfig = KalendarHeaderConfig(
                            kalendarTextConfig = KalendarTextConfig(
                                kalendarTextColor = KalendarTextColor(Color.Black),
                                kalendarTextSize = KalendarTextSize.SubTitle
                            )
                        ),
                        kalendarType = KalendarType.Firey,
                        kalendarDayColors = KalendarDayColors(Color.Black, Color.Black),
                        kalendarThemeColor = KalendarThemeColor(
                            backgroundColor = Color.White,
                            dayBackgroundColor = Color(0xffFBE3C7),
                            headerTextColor = Color.Black
                        ),
                        onCurrentDayClick = { kalendarDay: KalendarDay, kalendarEvents: List<KalendarEvent> ->

                            year = kalendarDay.localDate.year.toString()
                            month = kalendarDay.localDate.monthNumber.toString()
                            day = kalendarDay.localDate.dayOfMonth.toString()

                            val selectedDate = LocalDate.of(
                                kalendarDay.localDate.year,
                                kalendarDay.localDate.monthNumber,
                                kalendarDay.localDate.dayOfMonth
                            )
                            val dayOfWeek = selectedDate.dayOfWeek

                            dayString = when (dayOfWeek.value) {
                                1 -> "월요일"
                                2 -> "화요일"
                                3 -> "수요일"
                                4 -> "목요일"
                                5 -> "금요일"
                                6 -> "토요일"
                                7 -> "일요일"
                                else -> ""
                            }

                            scope.launch {
                                readTodo(token, year, month, day) {
                                    todoList.clear()
                                    for (i in it!!.data) {
                                        todoList.add(i)
                                    }
                                }
                            }
                        })
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 21.dp, end = 21.dp, top = 30.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = day,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 6.dp)
                )

                Text(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 18.sp,
                    color = Color(0xff9E9E9E),
                    text = dayString
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp, top = 1.dp),
                    color = Color(0xffdddbdb)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp, start = 21.dp, end = 21.dp)
            ) {
                if (isVisibility) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .focusRequester(focusRequester),
                        value = title,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0xffD8D8D8),
                            disabledLabelColor = Color(0xffD8D8D8),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        onValueChange = {
                            title = it
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            scope.launch {
                                createTodo(token, year, month, day, title, color) {
                                    readTodo(token, year = year, month = month, day = day) {
                                        todoList.clear()
                                        for (i in it!!.data) {
                                            todoList.add(i)
                                        }
                                    }
                                }
                                keyboardController?.hide()
                                title = ""
                                isVisibility = !isVisibility
                            }
                        })
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }

                if (todoList.isEmpty()) {
                    BlankTodoItem()
                }

                TodoItemList(Todo = todoList, todoList = todoList, onTodoItemClick = {
                    selectedTodo = it
                    scope.launch {
                        bottomScaffoldState.bottomSheetState.expand()
                    }
                })
            }
        }
    }
}

@Composable
fun AddTodoFloatingButton(
    multiFloatingState: FloatingStateType,
    onMultiFloatingStateChange: (FloatingStateType) -> Unit,
    backgroundColor: Color,
    onButtonClick: (String) -> Unit,
) {
    val transition = updateTransition(targetState = multiFloatingState, label = null)
    val rotate by transition.animateFloat(label = "rotate") {
        if (it == FloatingStateType.Expanded) {
            315f
        } else {
            0f
        }
    }
    Column(horizontalAlignment = Alignment.End) {

        AnimatedVisibility(
            visible = (multiFloatingState == FloatingStateType.Expanded),
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            FloatingActionButtonMenus(onMultiFloatingStateChange, onButtonClick)
        }

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        FloatingActionButton(
            modifier = Modifier.size(63.dp),
            containerColor = backgroundColor,
            shape = CircleShape,
            onClick = {
                onMultiFloatingStateChange(
                    if (transition.currentState == FloatingStateType.Expanded) {
                        FloatingStateType.Collapsed
                    } else {
                        FloatingStateType.Expanded
                    }
                )
            }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "todolist 추가",
                modifier = Modifier
                    .rotate(rotate)
                    .size(30.dp),
            )
        }
    }
}

@Composable
fun FloatingActionButtonMenus(
    onMultiFloatingStateChange: (FloatingStateType) -> Unit,
    onButtonClick: (String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .width(155.dp)
            .height(110.dp)
            .shadow(shape = RoundedCornerShape(20.dp), elevation = 5.dp)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffFFB4B4)),
                    onClick = {
                        onButtonClick("1")
                        onMultiFloatingStateChange(FloatingStateType.Collapsed)
                    },
                    content = {})
                Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffFFDCA8)),
                    onClick = {
                        onButtonClick("2")
                        onMultiFloatingStateChange(FloatingStateType.Collapsed)
                    },
                    content = {})
                Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffB1E0CF)),
                    onClick = {
                        onButtonClick("3")
                        onMultiFloatingStateChange(FloatingStateType.Collapsed)
                    },
                    content = {})
            }

            Spacer(modifier = Modifier.padding(vertical = 7.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffB7D7F5)),
                    onClick = {
                        onButtonClick("4")
                        onMultiFloatingStateChange(FloatingStateType.Collapsed)
                    },
                    content = {})
                Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffFFB8EB)),
                    onClick = {
                        onButtonClick("5")
                        onMultiFloatingStateChange(FloatingStateType.Collapsed)
                    },
                    content = {})
                Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffB6B1EC)),
                    onClick = {
                        onButtonClick("6")
                        onMultiFloatingStateChange(FloatingStateType.Collapsed)
                    },
                    content = {})
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Composable
fun TodoItem(
    Todo: RToDoResponse,
    onTodoItemClick: (RToDoResponse) -> Unit,
    onCheckedUpdateTodo: () -> Unit,
    onUnCheckedUpdateTodo: () -> Unit
) {
    var checked by rememberSaveable { mutableStateOf(Todo.done) }
    val token = "Token ${MyApplication.prefs.getData("token", "")}"
    var done by remember { mutableStateOf(false) }
    var scope = rememberCoroutineScope()

    LaunchedEffect(key1 = checked, block = {
        scope.launch {
            done = Todo.done
            if (checked) {
                done = true
                checked = true
                updateTodo(token,
                    Todo.year,
                    Todo.month,
                    Todo.day,
                    Todo.title,
                    done,
                    Todo.description,
                    Todo.color.toString(),
                    Todo.time,
                    Todo.id,
                    response = {
                        onCheckedUpdateTodo()
                    })
            }
        }
    })

    Card(colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .clickable {
                onTodoItemClick(Todo)
            }) {
        Row(
            modifier = Modifier.padding(start = 4.dp, top = 15.dp, bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Checkbox(
                checked = checked, onCheckedChange = { isChecked ->
                    checked = isChecked
                    done = isChecked
                    scope.launch {
                        updateTodo(
                            token,
                            Todo.year,
                            Todo.month,
                            Todo.day,
                            Todo.title,
                            done,
                            Todo.description,
                            Todo.color.toString(),
                            Todo.time,
                            Todo.id
                        ) {
                            when (isChecked) {
                                true -> onCheckedUpdateTodo()
                                false -> onUnCheckedUpdateTodo()
                            }
                        }
                    }
                }, colors = CheckboxDefaults.colors(
                    when (Todo.color) {
                        1 -> Color(0xffFFB4B4)
                        2 -> Color(0xffFFDCA8)
                        3 -> Color(0xffB1E0CF)
                        4 -> Color(0xffB7D7F5)
                        5 -> Color(0xffFFB8EB)
                        6 -> Color(0xffB6B1EC)
                        else -> Color.Black
                    }
                )

            )
            Text(text = Todo.title, fontSize = 14.sp, fontStyle = FontStyle.Normal)
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun TodoItemList(
    Todo: List<RToDoResponse>,
    todoList: MutableList<RToDoResponse>,
    onTodoItemClick: (RToDoResponse) -> Unit,
) {

    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    var categoryName by remember { mutableStateOf<ReadCategoryResponse?>(null) }

    var scope = rememberCoroutineScope()

    // 카테고리를 요청하는 함수를 호출하고, 응답을 categoryResponse에 저장합니다.

    LaunchedEffect(key1 = Unit, block = {
        scope.launch {
            readCategory(response = {
                categoryName = it
            })
        }
    })

    LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {

        val grouped = Todo.groupBy { it.color }

        grouped.forEach { (header, items) ->

            stickyHeader {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Button(
                        modifier = Modifier.size(9.dp),
                        onClick = { /*TODO*/ },
                        enabled = false,
                        content = {},
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = when (header) {
                                1 -> Color(0xffFFB4B4)
                                2 -> Color(0xffFFDCA8)
                                3 -> Color(0xffB1E0CF)
                                4 -> Color(0xffB7D7F5)
                                5 -> Color(0xffFFB8EB)
                                6 -> Color(0xffB6B1EC)
                                else -> Color.Black
                            }
                        )
                    )

                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                    Text(
                        text = categoryName?.data?.get(header.toString()) ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 17.sp
                    )
                }
            }
            items(items = items, key = { Todo -> Todo.id }) { item ->

                val dismissState = androidx.compose.material.rememberDismissState()
                val dismissDirection = dismissState.dismissDirection
                val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
                if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
                    scope.launch {
                        deleteTodo(token, item.id, response = {
                            todoList.remove(item)
                            readTodo(token, year = item.year, month = item.month, day = item.day) {
                                todoList.clear()
                                for (i in it!!.data) {
                                    todoList.add(i)
                                }
                            }
                        })
                    }
                }
                androidx.compose.material.SwipeToDismiss(state = dismissState,
                    background = { DeleteBackground() },
                    directions = setOf(DismissDirection.EndToStart),
                    dismissContent = {
                        TodoItem(Todo = item,
                            onTodoItemClick = { onTodoItemClick(it) },
                            onCheckedUpdateTodo = {
                                scope.launch {
                                    todoList.removeAll { it.id == item.id }
                                    todoList.add(item)
                                }
                            },
                            onUnCheckedUpdateTodo = {
                                scope.launch {
                                    todoList.removeAll { it.id == item.id }
                                    readTodo(
                                        token, year = item.year, month = item.month, day = item.day
                                    ) {
                                        todoList.clear()
                                        for (i in it!!.data) {
                                            todoList.add(i)
                                        }
                                    }.also {
                                        readTodo(
                                            token,
                                            year = item.year,
                                            month = item.month,
                                            day = item.day
                                        ) {
                                            todoList.clear()
                                            for (i in it!!.data) {
                                                if (i.done) {
                                                    todoList.removeAll { it.done == item.done }
                                                    todoList.add(item)
                                                }
                                            }
                                        }
                                    }.also {
                                        readTodo(
                                            token,
                                            year = item.year,
                                            month = item.month,
                                            day = item.day
                                        ) {
                                            todoList.clear()
                                            for (i in it!!.data) {
                                                todoList.add(i)
                                            }
                                        }
                                    }
                                }
                            })
                    },
                    dismissThresholds = {
                        androidx.compose.material.FractionalThreshold(fraction = 0.2f)
                    })
            }
        }
    }
}

@Composable
fun DeleteBackground() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(deleteBackground)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(text = "삭제", color = Color.White)
    }
}

@ExperimentalComposeUiApi
@SuppressLint("NewApi")
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun TodoUpdateBottomSheet(
    scope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    Todo: RToDoResponse,
    todoList: MutableList<RToDoResponse>,
) {

    var title by remember { mutableStateOf(Todo.title) }
    var description by remember { mutableStateOf(Todo.description) }
    var timeString by remember { mutableStateOf(Todo.time) }
    var time by remember { mutableStateOf(Todo.time) }

    var done by remember { mutableStateOf(Todo.done) }
    var color by remember { mutableStateOf(Todo.color) }
    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    var context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]
    var amPm by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(
        key1 = Todo.title,
        key2 = Todo.description,
        key3 = Todo.color,
        block = {
            scope.launch {
                title = Todo.title
                description = Todo.description
                color = Todo.color
            }
        })

    LaunchedEffect(key1 = Todo.time, key2 = Todo.done, block = {
//        timeValue = Todo.time

        if (Todo.time == "9999") {
            timeString = "미지정"
        } else {
            timeString = Todo.time
        }

        // 24 시간이 넘어가면 다른 숫자로 파악하여 빈값처리
        if(Todo.time != "미지정"){
            amPm = if (Todo.time.substring(0, 2).toInt() < 24) {
                //  12시간 미만이면 오전 아니면 오후
                if (Todo.time.substring(0, 2).toInt() <= 12) {
                    "오전"
                } else {
                    "오후"
                }
            } else {
               ""
            }
        }


        scope.launch {
            done = Todo.done
        }
    })

    val timePickerDialog = TimePickerDialog(
        context, { _, hour: Int, minute: Int ->
            amPm = if (hour < 12) "오전" else "오후"
            timeString = String.format("%02d%02d", hour, minute)
            time = String.format("%02d%02d", hour, minute)
        }, hour, minute, false
    )


    val onButtonClick: (String) -> Unit = { id ->
        when (id) {
            "1" -> {
                color = 1
            }
            "2" -> {
                color = 2
            }
            "3" -> {
                color = 3
            }
            "4" -> {
                color = 4
            }
            "5" -> {
                color = 5
            }
            "6" -> {
                color = 6
            }
        }
    }

    val categoryColor: (Int?) -> Color = {
        when (color) {
            1 -> {
                Color(0xffFFB4B4)
            }
            2 -> {
                Color(0xffFFDCA8)
            }
            3 -> {
                Color(0xffB1E0CF)
            }
            4 -> {
                Color(0xffB7D7F5)
            }
            5 -> {
                Color(0xffFFB8EB)
            }
            6 -> {
                Color(0xffB6B1EC)
            }
            else -> {
                Color.Black
            }
        }
    }

    Column(
        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight()
            .wrapContentSize()
            .padding(start = 25.dp, end = 25.dp, top = 20.dp)
            .imePadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            androidx.compose.material.IconButton(onClick = {
                scope.launch {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                    title = Todo.title
                    description = Todo.description
                    color = Todo.color
                    timeString = if(Todo.time == "9999") {
                        "미지정"
                    } else{
                        Todo.time
                    }
                }
            }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            }
            Button(
                modifier = Modifier
                    .width(60.dp)
                    .height(30.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
                onClick = {
                    scope.launch {
                        updateTodo(token,
                            Todo?.year.toString(),
                            Todo?.month.toString(),
                            Todo?.day.toString(),
                            title,
                            Todo?.done!!,
                            description,
                            color.toString(),
                            time,
                            Todo?.id.toString(),
                            response = {
                                readTodo(
                                    token,
                                    Todo?.year.toString(),
                                    Todo?.month.toString(),
                                    Todo?.day.toString()
                                ) {
                                    todoList.clear()
                                    for (i in it!!.data) {
                                        todoList.add(i)
                                    }
                                }
                            })
                        keyboardController?.hide()
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "저장",
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier
                    .width(9.dp)
                    .height(51.dp),
                onClick = { /*TODO*/ },
                enabled = false,
                content = {},
                colors = ButtonDefaults.buttonColors(disabledContainerColor = categoryColor(Todo?.color))
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = "${Todo?.month}월 ${Todo?.day}일",
                    fontSize = 15.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = categoryColor(color)
                )
                BasicTextField(modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                    value = title,
                    onValueChange = { title = it },
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 31.sp
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (title.isEmpty()) {
                                Text(
                                    text = "토도리스트 입력",
                                    fontSize = 24.sp,
                                    lineHeight = 31.2.sp,
                                    fontWeight = FontWeight(700),
                                    color = Color(0xFFC8C8C8),
                                )
                            }
                            innerTextField()
                        }
                    })

            }
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 22.dp),
            value = description,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xffF2F2F2),
                disabledLabelColor = Color(0xffF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(10.dp),
            onValueChange = {
                if (it.count { it == '\n' } < 4) {
                    description = it
                }
            },
            maxLines = 4,
            placeholder = {
                Text(
                    text = "+  메모하고 싶은 내용이 있나요?",
                    fontSize = 14.sp,
                    lineHeight = 19.6.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF9E9E9E)
                )
            },
            textStyle = TextStyle(fontSize = 14.sp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.clock),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 5.dp)
            )
            Text(
                text = "시간",
                modifier = Modifier.padding(end = 8.dp),
                fontWeight = FontWeight.Bold,
                lineHeight = 19.sp,
                fontSize = 15.sp
            )
            Text(
                modifier = Modifier.clickable {
                    timePickerDialog.show()
                },
                text = "$amPm ${convertToLayoutTimeFormat(timeString)}",
                lineHeight = 19.sp,
                fontSize = 15.sp,
                color = Color(0xff9E9E9E)
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, bottom = 19.dp),
            color = Color(0xffe9e9e9)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        onButtonClick("1")
                    },
                painter = if (color == 1) {
                    painterResource(id = R.drawable.redselecbutton)
                                          } else {
                    painterResource(id = R.drawable.redbutton)
                                                 },
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        onButtonClick("2")
                    },
                painter = if(color == 2) {
                    painterResource(id = R.drawable.yellowselecbutton)
                                         } else{
                    painterResource(id = R.drawable.yellowbutton)
                                               },
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        onButtonClick("3")
                    },
                painter = if(color == 3) {
                    painterResource(id = R.drawable.greenselecbutton)
                                         } else{
                    painterResource(id = R.drawable.greenbutton)
                                               },
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        onButtonClick("4")
                    },
                painter = if(color == 4) {
                                         painterResource(id = R.drawable.blueselecbutton)
                                         } else{
                    painterResource(id = R.drawable.bluebutton)
                                               },
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        onButtonClick("5")
                    },
                painter = if(color == 5) {
                                         painterResource(id = R.drawable.pinkselecbutton)
                                         } else{
                    painterResource(id = R.drawable.pinkbutton)
                                               },
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        onButtonClick("6")
                    },
                painter = if(color == 6){
                                        painterResource(id = R.drawable.purpleselecbutton)
                                        } else{
                    painterResource(id = R.drawable.purplebutton)
                                              },
                contentDescription = null
            )
        }
    }
}

@Composable
fun BlankTodoItem() {
    Card(
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 20.dp, top = 15.dp, bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "등록된 토도리스트가 없습니다.",
                fontSize = 13.sp,
                fontStyle = FontStyle.Normal,
                color = Color(0xff9e9e9e)
            )
        }
    }
}

fun convertToLayoutTimeFormat(time: String): String {
    return if (time == "미지정") {
        "미지정"
    } else if (time.length == 4) {
        "${time.substring(0, 2)}:${time.substring(2)}"
    } else {
        "${time.substring(0, 1)}:${time.substring(1)}"
    }
}