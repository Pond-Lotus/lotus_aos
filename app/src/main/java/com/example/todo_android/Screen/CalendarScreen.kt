package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.DismissDirection
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.himanshoe.kalendar.KalendarEvent
import com.himanshoe.kalendar.KalendarType
import com.himanshoe.kalendar.color.KalendarColor
import com.himanshoe.kalendar.color.KalendarColors
import com.himanshoe.kalendar.ui.component.day.KalendarDayKonfig
import com.himanshoe.kalendar.ui.component.header.KalendarTextKonfig
import com.himanshoe.kalendar.ui.firey.DaySelectionMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.number
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    var retrofit = Retrofit.Builder().baseUrl("https://team-lotus.kr/ ").client(okHttpClient)
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

    var retrofit = Retrofit.Builder().baseUrl("https://team-lotus.kr/ ").client(okHttpClient)
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

    var retrofit = Retrofit.Builder().baseUrl("https://team-lotus.kr/ ").client(okHttpClient)
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

    var retrofit = Retrofit.Builder().baseUrl("https://team-lotus.kr/ ").client(okHttpClient)
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

    var retrofit = Retrofit.Builder().baseUrl("https://team-lotus.kr/ ").client(okHttpClient)
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

    val selectedDate = LocalDate.of(
        year.toInt(), month.toInt(), day.toInt()
    )
    val dayOfWeek = selectedDate.dayOfWeek

    val dayColor: Color = when (dayOfWeek.value) {
        1 -> Color.Black
        2 -> Color.Black
        3 -> Color.Black
        4 -> Color.Black
        5 -> Color.Black
        6 -> Color.Black
        7 -> Color(0xFFF86B6B)
        else -> Color.Black
    }

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
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        scaffoldState = bottomScaffoldState,
        drawerContent = {
            ProfileModalDrawer(
                scope = scope, bottomScaffoldState = bottomScaffoldState, routeAction = routeAction
            )
        },
        topBar = {
            CenterAlignedTopAppBar(title = {
                MonthWeekToggleSwitch(
                    width = 105, height = 35, animateState = animateState
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
                    painter = painterResource(id = R.drawable.menubar),
                    contentDescription = "menubar"
                )
            }, colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.White, titleContentColor = Color.Black
            )
            )
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
        ) {
            Column(
                modifier = Modifier
                    .shadow(
                        shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp),
                        elevation = 3.dp,
                        spotColor = Color(0x0D000000),
                        ambientColor = Color(0x0D000000)
                    )
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300
                        )
                    )
            ) {
                if (!animateState.value) {
                    Kalendar(
                        currentDay = null,
                        kalendarType = KalendarType.Firey,
                        kalendarHeaderTextKonfig = KalendarTextKonfig(
                            kalendarTextColor = Color.Black, kalendarTextSize = 22.sp
                        ),
                        kalendarColors = KalendarColors(color = List(12) {
                            KalendarColor(
                                backgroundColor = Color.White,
                                dayBackgroundColor = Color(0xFFFFDAB9),
                                headerTextColor = Color.Black
                            )
                        }),
                        kalendarDayKonfig = KalendarDayKonfig(
                            size = 56.dp,
                            textSize = 13.sp,
                            textColor = Color.Black,
                            selectedTextColor = Color.Black,
                            borderColor = Color.Transparent
                        ),
                        daySelectionMode = DaySelectionMode.Single,
                        onDayClick = { localDate: kotlinx.datetime.LocalDate, kalendarEvents: List<KalendarEvent> ->
                            year = localDate.year.toString()
                            month = localDate.monthNumber.toString()
                            day = localDate.dayOfMonth.toString()

                            val selectedDate = LocalDate.of(
                                localDate.year, localDate.monthNumber, localDate.dayOfMonth
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
                        },
                        headerContent = { month, year ->
                            Row(
                                modifier = Modifier.padding(start = 20.dp, bottom = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Image(
                                    modifier = Modifier.size(22.dp), painter = painterResource(
                                        id = R.drawable.headertextemogi
                                    ), contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "${year}년 ${month.number}월",
                                    fontSize = 22.sp,
                                    lineHeight = 28.6.sp,
                                    fontWeight = FontWeight(700),
                                    color = Color(0xFF000000)
                                )
                            }
                        },
                        showLabel = true
                    )
                } else {
                    Kalendar(
                        currentDay = null,
                        kalendarType = KalendarType.Oceanic,
                        kalendarHeaderTextKonfig = KalendarTextKonfig(
                            kalendarTextColor = Color.Black, kalendarTextSize = 22.sp
                        ),
                        kalendarColors = KalendarColors(color = List(12) {
                            KalendarColor(
                                backgroundColor = Color.White,
                                dayBackgroundColor = Color(0xFFFFDAB9),
                                headerTextColor = Color.Black
                            )
                        }),
                        kalendarDayKonfig = KalendarDayKonfig(
                            size = 56.dp,
                            textSize = 13.sp,
                            textColor = Color.Black,
                            selectedTextColor = Color.Black,
                            borderColor = Color.Transparent
                        ),
                        daySelectionMode = DaySelectionMode.Single,
                        onDayClick = { localDate: kotlinx.datetime.LocalDate, kalendarEvents: List<KalendarEvent> ->
                            year = localDate.year.toString()
                            month = localDate.monthNumber.toString()
                            day = localDate.dayOfMonth.toString()

                            val selectedDate = LocalDate.of(
                                localDate.year, localDate.monthNumber, localDate.dayOfMonth
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
                        },
                        headerContent = { month, year ->
                            Row(
                                modifier = Modifier.padding(start = 20.dp, bottom = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Image(
                                    modifier = Modifier.size(22.dp), painter = painterResource(
                                        id = R.drawable.headertextemogi
                                    ), contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "${year}년 ${month.number}월",
                                    fontSize = 22.sp,
                                    lineHeight = 28.6.sp,
                                    fontWeight = FontWeight(700),
                                    color = Color(0xFF000000)
                                )
                            }
                        },
                        showLabel = true
                    )
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

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp),
                        colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 12.dp, top = 13.dp, bottom = 13.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.blankcheckbox),
                                contentDescription = null
                            )

                            BasicTextField(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .wrapContentWidth()
                                    .wrapContentHeight()
                                    .focusRequester(focusRequester),
                                value = title,
                                onValueChange = { title = it },
                                textStyle = TextStyle(
                                    fontSize = 13.sp,
                                    fontStyle = FontStyle.Normal,
                                    color = Color.Black,
                                    lineHeight = 31.sp
                                ),
                                singleLine = true,
                                maxLines = 1,
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
                        }
                    }
                }

                if (todoList.isEmpty() && !isVisibility) {
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
    val NextButtonArrowTint = when (multiFloatingState) {
        FloatingStateType.Expanded -> {
            Color(0xFFFFFFFF).copy(alpha = 1f)
        }
        FloatingStateType.Collapsed -> {
            Color(0xFF424242).copy(alpha = 1f)
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

        FloatingActionButton(modifier = Modifier.size(60.dp),
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
                modifier = Modifier
                    .rotate(rotate)
                    .size(30.dp)
                    .background(Color.Transparent),
                painter = painterResource(id = R.drawable.todolistaddemogi),
                contentDescription = null,
                tint = NextButtonArrowTint
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
    val swipeableState = rememberSwipeableState(initialValue = 0)

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
//            .swipeable(
//                state = swipeableState,
//                anchors = mapOf(
//                    0f to 0,
//                    -dipToPix(
//                        LocalContext.current,
//                        dipValue = 100f) to 1,
//                    dipToPix(
//                        LocalContext.current,
//                        dipValue = 100f) to 2
//                ),
//                thresholds = { _, _ ->
//                    FractionalThreshold(0.3f)
//                },
//                orientation = Orientation.Horizontal
//            )
            .clickable {
                onTodoItemClick(Todo)
            }) {
        Row(
            modifier = Modifier.padding(
                start = 18.dp, top = 13.dp, bottom = 13.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(painterResource(getCheckboxImageResource(checked, Todo.color)),
                contentDescription = "custom checkbox",
                modifier = Modifier.clickable {
                    checked = !checked;
                    done = checked;
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
                            when (checked) {
                                true -> onCheckedUpdateTodo();
                                false -> onUnCheckedUpdateTodo();
                            }
                        }
                    }

                })

            Text(
                modifier = Modifier.padding(start = 6.dp),
                text = Todo.title,
                fontSize = 13.sp,
                fontStyle = FontStyle.Normal
            )
        }
    }
}

@Composable
fun getCheckboxImageResource(checked: Boolean, color: Int): Int {
    return if (!checked) {
        R.drawable.defaultcheckbox;
    } else {
        when (color) {
            1 -> R.drawable.redcheckbox;
            2 -> R.drawable.yellowcheckbox;
            3 -> R.drawable.greencheckbox;
            4 -> R.drawable.bluecheckbox;
            5 -> R.drawable.pinkcheckbox;
            6 -> R.drawable.purplecheckbox;
            else -> R.drawable.defaultcheckbox
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

                    Button(modifier = Modifier.size(9.dp),
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

            itemsIndexed(items = items, key = { index, item -> item.id }) { index, item ->
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

            //                TodoItem(Todo = item,
//                    onTodoItemClick = { onTodoItemClick(it) },
//                    onCheckedUpdateTodo = {
//                        scope.launch {
//                            todoList.removeAll { it.id == item.id }
//                            todoList.add(item)
//                        }
//                    },
//                    onUnCheckedUpdateTodo = {
//                        scope.launch {
//                            todoList.removeAll { it.id == item.id }
//                            readTodo(
//                                token, year = item.year, month = item.month, day = item.day
//                            ) {
//                                todoList.clear()
//                                for (i in it!!.data) {
//                                    todoList.add(i)
//                                }
//                            }.also {
//                                readTodo(
//                                    token, year = item.year, month = item.month, day = item.day
//                                ) {
//                                    todoList.clear()
//                                    for (i in it!!.data) {
//                                        if (i.done) {
//                                            todoList.removeAll { it.done == item.done }
//                                            todoList.add(item)
//                                        }
//                                    }
//                                }
//                            }.also {
//                                readTodo(
//                                    token, year = item.year, month = item.month, day = item.day
//                                ) {
//                                    todoList.clear()
//                                    for (i in it!!.data) {
//                                        todoList.add(i)
//                                    }
//                                }
//                            }
//                        }
//                    })
            }


//            items(items = items, key = { Todo -> Todo.id }) { item ->
//
//                val dismissState = androidx.compose.material.rememberDismissState()
//                val dismissDirection = dismissState.dismissDirection
//                val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
//                if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
//                    scope.launch {
//                        deleteTodo(token, item.id, response = {
//                            todoList.remove(item)
//                            readTodo(token, year = item.year, month = item.month, day = item.day) {
//                                todoList.clear()
//                                for (i in it!!.data) {
//                                    todoList.add(i)
//                                }
//                            }
//                        })
//                    }
//                }
//                androidx.compose.material.SwipeToDismiss(state = dismissState,
//                    background = { DeleteBackground() },
//                    directions = setOf(DismissDirection.EndToStart),
//                    dismissContent = {
//                        TodoItem(Todo = item,
//                            onTodoItemClick = { onTodoItemClick(it) },
//                            onCheckedUpdateTodo = {
//                                scope.launch {
//                                    todoList.removeAll { it.id == item.id }
//                                    todoList.add(item)
//                                }
//                            },
//                            onUnCheckedUpdateTodo = {
//                                scope.launch {
//                                    todoList.removeAll { it.id == item.id }
//                                    readTodo(
//                                        token, year = item.year, month = item.month, day = item.day
//                                    ) {
//                                        todoList.clear()
//                                        for (i in it!!.data) {
//                                            todoList.add(i)
//                                        }
//                                    }.also {
//                                        readTodo(
//                                            token,
//                                            year = item.year,
//                                            month = item.month,
//                                            day = item.day
//                                        ) {
//                                            todoList.clear()
//                                            for (i in it!!.data) {
//                                                if (i.done) {
//                                                    todoList.removeAll { it.done == item.done }
//                                                    todoList.add(item)
//                                                }
//                                            }
//                                        }
//                                    }.also {
//                                        readTodo(
//                                            token,
//                                            year = item.year,
//                                            month = item.month,
//                                            day = item.day
//                                        ) {
//                                            todoList.clear()
//                                            for (i in it!!.data) {
//                                                todoList.add(i)
//                                            }
//                                        }
//                                    }
//                                }
//                            })
//                    },
//                    dismissThresholds = {
//                        androidx.compose.material.FractionalThreshold(fraction = 0.2f)
//                    })
//            }
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
    bottomSheetScaffoldState: androidx.compose.material.BottomSheetScaffoldState,
    Todo: RToDoResponse,
    todoList: MutableList<RToDoResponse>,
) {

    var title by remember { mutableStateOf(Todo.title) }
    var description by remember { mutableStateOf(Todo.description) }
    var timeString by remember { mutableStateOf(Todo.time) }
    var time by remember { mutableStateOf(Todo.time) }

    var done by remember { mutableStateOf(Todo.done) }
    var color by remember { mutableStateOf(Todo.color) }
    var dayString by remember { mutableStateOf("") }
    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    var context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]
    var amPm by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val timePickerDialog = TimePickerDialog(
        context, R.style.TimePickerDialog, { _, hour: Int, minute: Int ->
            amPm = if (hour < 12) "오전" else "오후"
            timeString = String.format("%02d%02d", hour, minute)
            time = String.format("%02d%02d", hour, minute)
        }, hour, minute, false
    )

    val selectedDate = LocalDate.of(
        Todo.year.toInt(), Todo.month.toInt(), Todo.day.toInt()
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

    LaunchedEffect(key1 = Todo.title, key2 = Todo.description, key3 = Todo.color, block = {
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
        if (Todo.time != "미지정") {
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

    LaunchedEffect(bottomSheetScaffoldState.bottomSheetState.currentValue, block = {
        if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    })

    Column(
        modifier = Modifier
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

            Image(
                modifier = Modifier.clickable {
                    scope.launch {
                        title = Todo.title
                        description = Todo.description
                        color = Todo.color
                        timeString = if (Todo.time == "9999") {
                            "미지정"
                        } else {
                            Todo.time
                        }
                        keyboardController?.hide()
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }, painter = painterResource(id = R.drawable.close), contentDescription = null
            )

            Box(modifier = Modifier
                .width(60.dp)
                .height(30.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(Color(0xffFFBE3C7))
                .clickable {
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
                }) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
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
                    text = "${Todo.month}월 ${Todo.day}일 ${dayString}",
                    fontSize = 15.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = categoryColor(color)
                )
                BasicTextField(modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .focusRequester(focusRequester),
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
                    }, painter = if (color == 1) {
                    painterResource(id = R.drawable.redselecbutton)
                } else {
                    painterResource(id = R.drawable.redbutton)
                }, contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        onButtonClick("2")
                    }, painter = if (color == 2) {
                    painterResource(id = R.drawable.yellowselecbutton)
                } else {
                    painterResource(id = R.drawable.yellowbutton)
                }, contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        onButtonClick("3")
                    }, painter = if (color == 3) {
                    painterResource(id = R.drawable.greenselecbutton)
                } else {
                    painterResource(id = R.drawable.greenbutton)
                }, contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        onButtonClick("4")
                    }, painter = if (color == 4) {
                    painterResource(id = R.drawable.blueselecbutton)
                } else {
                    painterResource(id = R.drawable.bluebutton)
                }, contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        onButtonClick("5")
                    }, painter = if (color == 5) {
                    painterResource(id = R.drawable.pinkselecbutton)
                } else {
                    painterResource(id = R.drawable.pinkbutton)
                }, contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        onButtonClick("6")
                    }, painter = if (color == 6) {
                    painterResource(id = R.drawable.purpleselecbutton)
                } else {
                    painterResource(id = R.drawable.purplebutton)
                }, contentDescription = null
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
            modifier = Modifier.padding(start = 12.dp, top = 13.dp, bottom = 13.dp),
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

private fun dipToPix(context: Context, dipValue: Float): Float {
    return dipValue * context.resources.displayMetrics.density
}