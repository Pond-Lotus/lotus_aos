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
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.kizitonwose.calendar.compose.*
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*
import java.util.Calendar.*
import androidx.compose.material3.Card
import com.example.todo_android.Util.rememberFirstVisibleMonthAfterScroll
import com.example.todo_android.Util.rememberFirstVisibleWeekAfterScroll
import java.time.YearMonth

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

@OptIn(ExperimentalAnimationApi::class)
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

    var animateState = remember { mutableStateOf(true) }

    var multiFloatingState by remember { mutableStateOf(FloatingStateType.Collapsed) }
    val colorFAB = if (multiFloatingState == FloatingStateType.Expanded) {
        Color(0xff9E9E9E)
    } else {
        Color(0xffFFDAB9)
    }


    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    var todoYear by remember { mutableStateOf(LocalDate.now().year.toString()) }
    var todoMonth by remember { mutableStateOf(LocalDate.now().monthValue.toString()) }
    var todoDay by remember { mutableStateOf(LocalDate.now().dayOfMonth.toString()) }
    var todoTitle by remember { mutableStateOf("") }
    var todoColor by remember { mutableStateOf("") }

    var dayString by remember { mutableStateOf("") }
    var todoList = remember { mutableStateListOf<RToDoResponse>() }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()
    val bottomScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))

    var selectedTodoListItem by remember { mutableStateOf<RToDoResponse?>(null) }

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    var currentDate = remember { LocalDate.now() }

    val startDate = remember { currentDate.minusYears(2) }

    val endDate = remember { currentDate.plusYears(2) }

    val weekState = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
        firstDayOfWeek = DayOfWeek.MONDAY
    )

    val monthState = rememberCalendarState(
        startMonth = startDate.yearMonth,
        endMonth = endDate.yearMonth,
        firstVisibleMonth = currentDate.yearMonth,
        firstDayOfWeek = DayOfWeek.MONDAY
    )
    val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)

    val week = rememberFirstVisibleWeekAfterScroll(state = weekState)
    val month = rememberFirstVisibleMonthAfterScroll(state = monthState)

    val onButtonClick: (String) -> Unit = { id ->
        when (id) {
            "1" -> {
                isVisibility = !isVisibility
                todoColor = "1"
                Log.d("id", "id : ${id}")
            }
            "2" -> {
                isVisibility = !isVisibility
                todoColor = "2"
                Log.d("id", "id : ${id}")
            }
            "3" -> {
                isVisibility = !isVisibility
                todoColor = "3"
                Log.d("id", "id : ${id}")
            }
            "4" -> {
                isVisibility = !isVisibility
                todoColor = "4"
                Log.d("id", "id : ${id}")
            }
            "5" -> {
                isVisibility = !isVisibility
                todoColor = "5"
                Log.d("id", "id : ${id}")
            }
            "6" -> {
                isVisibility = !isVisibility
                todoColor = "6"
                Log.d("id", "id : ${id}")
            }
        }
    }

    LaunchedEffect(key1 = Unit, block = {
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

    LaunchedEffect(isVisibility) {
        if (isVisibility) {
            focusRequester.requestFocus()
        }
    }

    LaunchedEffect(key1 = month) {
        selectedDate = month.yearMonth.atStartOfMonth()

        val dayOfWeek = LocalDate.of(
            selectedDate.year,
            selectedDate.monthValue,
            selectedDate.dayOfMonth
        ).dayOfWeek

        dayString = when (dayOfWeek.value) {
            1 -> "월요일"
            2 -> "화요일"
            3 -> "수요일"
            4 -> "목요일"
            5 -> "금요일"
            6 -> "토요일"
            7 -> "일요일"
            else -> null.toString()
        }
        scope.launch {
            readTodo(
                token,
                selectedDate.year.toString(),
                selectedDate.monthValue.toString(),
                selectedDate.dayOfMonth.toString()
            ) {
                todoList.clear()
                for (i in it!!.data) {
                    todoList.add(i)
                }
            }
        }
    }

    LaunchedEffect(key1 = week) {
        selectedDate = if (week.days.contains(WeekDay(currentDate, WeekDayPosition.RangeDate))) {
            currentDate
        } else {
            week.days.first().date
        }

        val dayOfWeek = LocalDate.of(
            selectedDate.year,
            selectedDate.monthValue,
            selectedDate.dayOfMonth
        ).dayOfWeek

        dayString = when (dayOfWeek.value) {
            1 -> "월요일"
            2 -> "화요일"
            3 -> "수요일"
            4 -> "목요일"
            5 -> "금요일"
            6 -> "토요일"
            7 -> "일요일"
            else -> null.toString()
        }
        scope.launch {
            readTodo(
                token,
                selectedDate.year.toString(),
                selectedDate.monthValue.toString(),
                selectedDate.dayOfMonth.toString()
            ) {
                todoList.clear()
                for (i in it!!.data) {
                    todoList.add(i)
                }
            }
        }
    }

    BottomSheetScaffold(
        modifier = Modifier
            .imePadding(),
        scaffoldState = bottomScaffoldState,
        drawerContent = {
            ProfileModalDrawer(
                scope = scope, routeAction = routeAction
            )
        },
        drawerGesturesEnabled = true,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    MonthWeekToggleSwitch(
                        width = 105,
                        height = 35,
                        animateState = animateState,
                        onChangeCalendar = {
                            animateState.value = !animateState.value
                            scope.launch {
                                if (animateState.value) {
                                    monthState.animateScrollToMonth(currentDate.yearMonth)
                                    Log.d("scrolltest", selectedDate.toString())
                                } else {
                                    weekState.animateScrollToWeek(currentDate)
                                    Log.d("scrolltest", selectedDate.toString())
                                }
                            }
                        }
                    )
                }, navigationIcon = {
                    Image(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(start = 17.dp)
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
                multiFloatingState = multiFloatingState,
                onMultiFloatingStateChange = { multiFloatingState = it },
                backgroundColor = colorFAB,
                onButtonClick = onButtonClick
            )
        },
        floatingActionButtonPosition = androidx.compose.material.FabPosition.End,
        sheetPeekHeight = 1.dp,
        sheetShape = RoundedCornerShape(
            topStart = 20.dp, topEnd = 20.dp
        ),
        sheetContent = {
            selectedTodoListItem?.let {
                TodoUpdateBottomSheet(
                    scope,
                    bottomScaffoldState,
                    it,
                    todoList
                )
            }
        }
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
                        elevation = 7.dp,
                        spotColor = Color(0xffB0B0B0),
                        ambientColor = Color(0xffB0B0B0)
                    )
            ) {
                CalendarTitle(yearMonth = selectedDate.yearMonth)

                CalendarHeader(daysOfWeek = daysOfWeek)

                CalendarContent(
                    selectedDate = selectedDate,
                    weekState = weekState,
                    monthState = monthState,
                    isMonthMode = animateState.value,
                    onSelectedDate = {

                        selectedDate = it

                        val dayOfWeek =
                            LocalDate.of(it.year, it.monthValue, it.dayOfMonth).dayOfWeek

                        dayString = when (dayOfWeek.value) {
                            1 -> "월요일"
                            2 -> "화요일"
                            3 -> "수요일"
                            4 -> "목요일"
                            5 -> "금요일"
                            6 -> "토요일"
                            7 -> "일요일"
                            else -> null.toString()
                        }
                        scope.launch {
                            readTodo(
                                token,
                                it.year.toString(),
                                it.monthValue.toString(),
                                it.dayOfMonth.toString()
                            ) {
                                todoList.clear()
                                for (i in it!!.data) {
                                    todoList.add(i)
                                }
                            }
                        }
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 21.dp, end = 21.dp, top = 30.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (
                        selectedDate.dayOfMonth.toString().length == 1
                    ) {
                        "0${selectedDate.dayOfMonth}"
                    } else {
                        "${selectedDate.dayOfMonth}"
                    },
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
                                value = todoTitle,
                                onValueChange = { todoTitle = it },
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
                                        createTodo(
                                            token,
                                            todoYear,
                                            todoMonth,
                                            todoDay,
                                            todoTitle,
                                            todoColor
                                        ) {
                                            readTodo(
                                                token,
                                                year = todoYear,
                                                month = todoMonth,
                                                day = todoDay
                                            ) {
                                                todoList.clear()
                                                for (i in it!!.data) {
                                                    todoList.add(i)
                                                }
                                            }
                                        }
                                        keyboardController?.hide()
                                        todoTitle = ""
                                        isVisibility = !isVisibility
                                    }
                                })
                            )
                        }
                    }
                }

                if (todoList.isEmpty() && !isVisibility) {
                    Row {
                        BlankTodoItem()
                    }

                }

                TodoItemList(Todo = todoList, todoList = todoList, onTodoItemClick = {
                    selectedTodoListItem = it
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

    val rotate by animateFloatAsState(
        targetValue = if (multiFloatingState == FloatingStateType.Expanded) 45f else 0f,
        animationSpec = tween(durationMillis = 400) // 여기서 애니메이션 지속 시간을 설정할 수 있습니다.
    )

    val NextButtonArrowTint = when (multiFloatingState) {
        FloatingStateType.Expanded -> {
            Color(0xFFFFFFFF).copy(alpha = 1f)
        }
        FloatingStateType.Collapsed -> {
            Color(0xFF424242).copy(alpha = 1f)
        }
    }

    Box(
        modifier = Modifier.padding(
            end = 20.dp,
//            bottom = 150.dp
        ),
        contentAlignment = Alignment.CenterEnd
//        horizontalAlignment = Alignment.End
    ) {

        AnimatedVisibility(
            visible = (multiFloatingState == FloatingStateType.Expanded),
            enter = fadeIn(
                animationSpec = tween(500)
            ) + slideInVertically(
                animationSpec = tween(500),
                initialOffsetY = {
                    it / 8
                }
            ),
            exit = fadeOut(
                animationSpec = tween(500)
            ) + slideOutVertically(
                animationSpec = tween(500),
                targetOffsetY = {
                    it / 8
                }
            )
        ) {
            FloatingActionButtonMenus(onMultiFloatingStateChange, onButtonClick)
        }

        FloatingActionButton(
            modifier = Modifier
                .padding(bottom = 18.dp)
                .size(65.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    spotColor = Color(0xff9E9E9E),
                    ambientColor = Color(0xffACACAC)
                ),
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
                    .size(32.dp)
                    .background(Color.Transparent),
                painter = painterResource(id = R.drawable.todolistaddemogi),
                contentDescription = null,
                tint = NextButtonArrowTint
            )
        }
    }


//    Column(
//        modifier = Modifier.padding(end = 20.dp, bottom = 150.dp),
//        horizontalAlignment = Alignment.End
//    ) {
//
//        AnimatedVisibility(
//            visible = (multiFloatingState == FloatingStateType.Expanded),
//            enter = fadeIn(
//                animationSpec = tween(500)
//            ) + slideInVertically(
//                animationSpec = tween(500),
//                initialOffsetY = {
//                    it / 8
//                }
//            ),
//            exit = fadeOut(
//                animationSpec = tween(500)
//            ) + slideOutVertically(
//                animationSpec = tween(500),
//                targetOffsetY = {
//                    it / 8
//                }
//            )
//        ) {
//            FloatingActionButtonMenus(onMultiFloatingStateChange, onButtonClick)
//        }
//
//        FloatingActionButton(
//            modifier = Modifier
//                .padding(bottom = 18.dp)
//                .size(65.dp)
//                .shadow(
//                    elevation = 4.dp,
//                    shape = CircleShape,
//                    spotColor = Color(0xff9E9E9E),
//                    ambientColor = Color(0xffACACAC)
//                ),
//            containerColor = backgroundColor,
//            shape = CircleShape,
//            onClick = {
//                onMultiFloatingStateChange(
//                    if (transition.currentState == FloatingStateType.Expanded) {
//                        FloatingStateType.Collapsed
//                    } else {
//                        FloatingStateType.Expanded
//                    }
//                )
//            }) {
//            Icon(
//                modifier = Modifier
//                    .rotate(rotate)
//                    .size(32.dp)
//                    .background(Color.Transparent),
//                painter = painterResource(id = R.drawable.todolistaddemogi),
//                contentDescription = null,
//                tint = NextButtonArrowTint
//            )
//        }
//    }
}

@Composable
fun FloatingActionButtonMenus(
    onMultiFloatingStateChange: (FloatingStateType) -> Unit,
    onButtonClick: (String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .offset(y = -110.dp)
            .width(155.dp)
            .height(110.dp)
            .padding(bottom = 10.dp)
            .shadow(
                shape = RoundedCornerShape(20.dp),
                elevation = 5.dp,
                spotColor = Color(0xff9E9E9E),
                ambientColor = Color(0xffACACAC)
            )
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
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
            .clickable {
                onTodoItemClick(Todo)
            }) {
        Row(
            modifier = Modifier.padding(
                start = 14.dp, top = 13.dp, bottom = 13.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(painterResource(getCheckboxImageResource(checked, Todo.color)),
                contentDescription = "custom checkbox",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
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
                modifier = Modifier
                    .padding(start = 6.dp),
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                text = Todo.title,
                fontSize = 15.sp,
                fontStyle = FontStyle.Normal,
                overflow = TextOverflow.Ellipsis
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

    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        val grouped = Todo.groupBy { it.color }
        var rowIndex = 0;

        grouped.forEach { (header, items) ->

            stickyHeader {
                Surface(
                    modifier = Modifier
                        .fillParentMaxWidth(),
                    color = Color(0xfff0f0f0)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = if (rowIndex++ > 0) {
                                    15.dp
                                } else {
                                    0.dp
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .clip(shape = CircleShape)
                                .background(
                                    color = when (header) {
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

                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = categoryName?.data?.get(header.toString()) ?: "",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            itemsIndexed(items = items, key = { index, Todo -> Todo.id }) { index, item ->
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
//                        item.itemIndex = rowIndex++;
//
//                        var listIndex = -1
//                        for (i in (0..(todoList.size - 1)).reversed()) {
//                            Log.d("index", i.toString())
//                            if (todoList.get(i).color == item.color
//                                && listIndex == -1
//                                && item.itemIndex != i
//                            ) {
//                                item.grpIndex = i
//                                listIndex = i
//
//                            }
//                        }

                        Log.d("list", todoList.toList().toString())


                        TodoItem(Todo = item,
                            onTodoItemClick = { onTodoItemClick(it) },
                            onCheckedUpdateTodo = {
                                scope.launch {
                                    todoList.removeAll { it.id == item.id }
//                                    todoList.add(item.grpIndex, item)

                                }
                            },
                            onUnCheckedUpdateTodo = {
                                scope.launch {
                                    todoList.removeAll { it.id == item.id }
//                                    todoList.add(item.itemIndex, item)
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

    LaunchedEffect(bottomSheetScaffoldState.bottomSheetState.currentValue, block = {
        if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    })

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
                .height(50.dp),
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
                .padding(top = 22.dp, bottom = 22.dp),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 19.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .size(30.dp)
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
                    .size(30.dp)
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
                    .size(30.dp)
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
                    .size(30.dp)
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
                    .size(30.dp)
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
                    .size(30.dp)
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
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                fontSize = 15.sp,
                fontStyle = FontStyle.Normal,
                color = Color(0xff9e9e9e)
            )
        }
    }
}

private fun convertToLayoutTimeFormat(time: String): String {
    return if (time == "미지정") {
        "미지정"
    } else if (time.length == 4) {
        "${time.substring(0, 2)}:${time.substring(2)}"
    } else {
        "${time.substring(0, 1)}:${time.substring(1)}"
    }
}

@Composable
fun Day(
    day: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: (LocalDate) -> Unit,
    isPastDay: Boolean
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .size(36.dp)
            .padding(10.dp)
            .clip(CircleShape)
            .background(
                color = if (isSelected) {
                    Color(0xFFFFDAB9)
                } else if (isToday) {
                    Color(0xffE9E9E9)
                } else {
                    Color.Transparent
                },
                CircleShape
            )
            .clickable(
                onClick = {
                    onClick(day)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.alpha(
                if (isPastDay) {
                    0.5f
                } else {
                    1f
                }
            ),
            text = day.dayOfMonth.toString(),
            color = when (day.dayOfWeek) {
                DayOfWeek.SUNDAY -> Color(0xFFF86B6B)
                else -> Color(
                    0xFF424242
                )
            },
            lineHeight = 17.sp,
            fontWeight = FontWeight(700)
        )
    }
}

@Composable
fun CalendarHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(
                    java.time.format.TextStyle.SHORT,
                    Locale.getDefault()
                ),
                lineHeight = 17.sp,
                fontWeight = FontWeight(700)
            )
        }
    }
}

@Composable
fun CalendarTitle(yearMonth: YearMonth) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(
                start = 22.dp,
                bottom = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            modifier = Modifier.size(22.dp),
            painter = painterResource(
                id = R.drawable.headertextemogi
            ),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = if (yearMonth.monthValue.toString().length == 2) {
                "${yearMonth.year}. ${yearMonth.monthValue}"
            } else {
                "${yearMonth.year}. 0${yearMonth.monthValue}"
            },
            fontSize = 22.sp,
            lineHeight = 28.6.sp,
            fontWeight = FontWeight(700),
            color = Color(0xFF000000)
        )
    }
}

@Composable
fun CalendarContent(
    selectedDate: LocalDate,
    weekState: WeekCalendarState,
    monthState: CalendarState,
    isMonthMode: Boolean,
    onSelectedDate: (LocalDate) -> Unit
) {
    AnimatedContent(
        targetState = isMonthMode,
        transitionSpec = {
            slideInVertically(
                initialOffsetY = { 0 },
                animationSpec = tween(easing = FastOutLinearInEasing)
            ) togetherWith slideOutVertically(
                targetOffsetY = { 0 },
                animationSpec = tween(easing = LinearOutSlowInEasing)
            )
        },
        content = {
            if (isMonthMode) {
                var start = selectedDate.yearMonth.atDay(1)
                var end = selectedDate.yearMonth.atEndOfMonth()

                HorizontalCalendar(
                    modifier = Modifier
                        .background(color = Color.White),
                    state = monthState,
                    dayContent = { day ->
                        Day(
                            day = day.date,
                            isSelected = selectedDate == day.date,
                            isToday = day.date == LocalDate.now(),
                            isPastDay = day.date < start || day.date > end,
                            onClick = {
                                onSelectedDate(day.date)
                            },
                        )
                    }
                )
            } else {
                var start = selectedDate.yearMonth.atDay(1)
                var end = selectedDate.yearMonth.atEndOfMonth()

                WeekCalendar(
                    modifier = Modifier
                        .background(color = Color.White),
                    state = weekState,
                    contentPadding = PaddingValues(0.dp),
                    dayContent = { day ->
                        Day(
                            day = day.date,
                            isSelected = selectedDate == day.date,
                            isToday = day.date == LocalDate.now(),
                            isPastDay = day.date < start || day.date > end,
                            onClick = {
                                onSelectedDate(day.date)
                            }
                        )
                    }
                )
            }
        }
    )
}

//private fun todoSortByDone(todoList: MutableList<RToDoResponse>): MutableList<RToDoResponse>{
//
//    var resultUnCheckedValue = mutableListOf<RToDoResponse>()
//    var resultCheckedValue = mutableListOf<RToDoResponse>()
//
//    var finalUnCheckedValue =  mutableListOf<RToDoResponse>()
//    var finalCheckedValue = mutableListOf<RToDoResponse>()
//
//    var returnValue = mutableListOf<RToDoResponse>()
//
//    todoList.forEach { response ->
//        var resultDone = response.done
//
//        if(!resultDone){
//            resultUnCheckedValue.add(response)
//        }else{
//            resultCheckedValue.add(response)
//        }
//    }
//
//    resultUnCheckedValue.sortedBy {
//
//        finalUnCheckedValue.add(it.id.toInt())
//    }
//
//    resultCheckedValue.sortedBy {
//
//        finalUnCheckedValue.add(it.id.toInt())
//    }
//
////    resultUnCheckedValue.sortBy { it.id.toInt() }
////
////    resultCheckedValue.sortBy { it.id.toInt() }
//
//    returnValue = (finalUnCheckedValue + finalCheckedValue).toMutableList()
//
//    return returnValue
//}