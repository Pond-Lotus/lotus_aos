package com.example.todo_android.screen

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.R
import com.example.todo_android.composable.FloatingStateType
import com.example.todo_android.composable.MonthWeekToggleSwitch
import com.example.todo_android.composable.ProfileModalDrawer
import com.example.todo_android.navigation.Action.RouteAction
import com.example.todo_android.request.CategoryRequest.ReadCategoryRequest
import com.example.todo_android.request.TodoRequest.CreateTodoRequest
import com.example.todo_android.request.TodoRequest.DeleteTodoRequest
import com.example.todo_android.request.TodoRequest.ReadTodoRequest
import com.example.todo_android.request.TodoRequest.UpdateTodoRequest
import com.example.todo_android.response.CategoryResponse.CategoryData
import com.example.todo_android.response.CategoryResponse.ReadCategoryResponse
import com.example.todo_android.response.TodoResponse.*
import com.example.todo_android.ui.theme.deleteBackground
import com.example.todo_android.util.MyApplication
import com.example.todo_android.util.rememberFirstVisibleMonthAfterScroll
import com.example.todo_android.util.rememberFirstVisibleWeekAfterScroll
import com.example.todo_android.viewmodel.Todo.TodoViewModel
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
import java.time.YearMonth
import java.util.*
import java.util.Calendar.*

fun createTodo(
    token: String,
    year: Int,
    month: Int,
    day: Int,
    title: String,
    color: Int,
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

    readTodoRequest.requestReadTodo(token, year.toInt(), month.toInt(), day.toInt())
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
        }
    })
}

fun updateTodo(
    token: String,
    year: Int,
    month: Int,
    day: Int,
    title: String,
    done: Boolean,
    description: String,
    color: Int,
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

    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    val vm: TodoViewModel = hiltViewModel()

    val todoYear by vm.todoYear.collectAsState()
    val todoMonth by vm.todoMonth.collectAsState()
    val todoDay by vm.todoDay.collectAsState()
    val todoTitle by vm.todoTitle.collectAsState()
    val todoColor by vm.todoColor.collectAsState()

    val todoList by vm.todoList.collectAsState()
    val categoryList by vm.categoryList.collectAsState()
    val categoryTodoList by vm.categoryTodoList.collectAsState()

    var isVisibility by remember { mutableStateOf(false) }

    var animateState = remember { mutableStateOf(true) }

    var multiFloatingState by remember { mutableStateOf(FloatingStateType.Collapsed) }
    val colorFAB = if (multiFloatingState == FloatingStateType.Expanded) {
        Color(0xff9E9E9E)
    } else {
        Color(0xffFFDAB9)
    }

    var dayString by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()
    val bottomScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))

//    var selectedTodoListItem by remember { mutableStateOf<RToDoResponse?>(null) }

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
                vm::setTodoColor.invoke(1)
                Log.d("id", "id : ${id}")
            }
            "2" -> {
                isVisibility = !isVisibility
                vm::setTodoColor.invoke(2)
                Log.d("id", "id : ${id}")
            }
            "3" -> {
                isVisibility = !isVisibility
                vm::setTodoColor.invoke(3)
                Log.d("id", "id : ${id}")
            }
            "4" -> {
                isVisibility = !isVisibility
                vm::setTodoColor.invoke(4)
                Log.d("id", "id : ${id}")
            }
            "5" -> {
                isVisibility = !isVisibility
                vm::setTodoColor.invoke(5)
                Log.d("id", "id : ${id}")
            }
            "6" -> {
                isVisibility = !isVisibility
                vm::setTodoColor.invoke(6)
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
            vm.readTodo(token, todoYear, todoMonth, todoDay)
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
            vm.readTodo(token, todoYear, todoMonth, todoDay)
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
            vm.readTodo(token, todoYear, todoMonth, todoDay)
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

        }
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xfff0f0f0)),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = rememberLazyListState()
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .shadow(
                                shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp),
                                elevation = 7.dp,
                                spotColor = Color(0xffB0B0B0),
                                ambientColor = Color(0xffB0B0B0)
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
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
                                    vm.readTodo(token, it.year, it.monthValue, it.dayOfMonth)
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
                }
            }

            TodoItem(
                vm = vm,
                token = token,
                categoryList = categoryList,
                categoryTodoList = categoryTodoList,
                scope = scope
            )

            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 21.dp, end = 21.dp)
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
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = 13.dp,
                                    bottom = 13.dp
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.blankcheckbox),
                                    contentDescription = null
                                )

                                BasicTextField(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .wrapContentHeight()
                                        .padding(start = 16.dp)
                                        .focusRequester(focusRequester)
                                        .imePadding(),
                                    value = todoTitle,
                                    onValueChange = { text: String ->
                                        vm::setTodoTitle.invoke(text)
                                    },
                                    textStyle = androidx.compose.material3.LocalTextStyle.current.copy(
                                        fontSize = 13.sp,
                                        fontStyle = FontStyle.Normal,
                                        color = Color.Black,
                                        lineHeight = 31.sp
                                    ),
                                    singleLine = true,
                                    maxLines = 1,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(onDone = {
                                        scope.launch {
                                            vm.createTodo(
                                                token,
                                                CreateTodo(
                                                    todoYear,
                                                    todoMonth,
                                                    todoDay,
                                                    todoTitle,
                                                    todoColor
                                                )
                                            )
                                            vm.setTodoTitle("")
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
                }
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

    Column(
        modifier = Modifier.padding(
            end = 20.dp,
            bottom = 150.dp
        ),
        horizontalAlignment = Alignment.End
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
fun CalendarTitle(yearMonth: YearMonth) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(
                vertical = 15.dp,
                horizontal = 35.dp
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
fun CalendarHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(
                start = 15.dp,
                end = 15.dp,
            )
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
                    },
                    contentPadding = PaddingValues(
                        start = 15.dp,
                        end = 15.dp
                    )
                )
            } else {
                var start = selectedDate.yearMonth.atDay(1)
                var end = selectedDate.yearMonth.atEndOfMonth()

                WeekCalendar(
                    modifier = Modifier
                        .background(color = Color.White),
                    state = weekState,
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
                    },
                    contentPadding = PaddingValues(
                        start = 15.dp,
                        end = 15.dp
                    )
                )
            }
        }
    )
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
//            .padding(13.dp)
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
            fontSize = 14.sp,
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

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
fun LazyListScope.TodoItem(
    vm: TodoViewModel,
    token: String,
    categoryList: List<CategoryData>,
    categoryTodoList: Map<Int?, List<TodoData>>,
    scope: CoroutineScope,
) {

    categoryTodoList.forEach { _, items ->
        stickyHeader {
            TodoCategoryHeader(
                categoryList = categoryList,
                items = items
            )
        }

        itemsIndexed(
            items = items,
            key = { index: Int, todo: TodoData -> todo.id!! }
        ) { index, item ->

            val dismissState = androidx.compose.material3.rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                scope.launch {
                    vm.deleteTodo(token, item)
                }
            }

            SwipeToDismiss(
                modifier = Modifier.padding(
                    start = 21.dp,
                    end = 21.dp
                ),
                state = dismissState,
                background = {
                    DeleteBackground()
                },
                directions = setOf(DismissDirection.EndToStart),
                dismissContent = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .clickable {
                                // bottomsheet 열기
                            },
                        colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 14.dp, top = 13.dp, bottom = 13.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        // todo 체크박스 클릭
                                    },
                                painter = painterResource(
                                    id = getCheckboxImageResource(
                                        checked = item.done!!,
                                        color = item.color!!
                                    )
                                ),
                                contentDescription = "checkboxImage"
                            )

                            Text(
                                modifier = Modifier.padding(start = 6.dp),
                                text = item.title!!,
                                fontSize = 15.sp,
                                fontStyle = FontStyle.Normal,
                                overflow = TextOverflow.Ellipsis,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun TodoCategoryHeader(
    categoryList: List<CategoryData>,
    items: List<TodoData>
) {

    val color = items.map { it.color }.last()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 21.dp,
                end = 21.dp
            ),
        color = Color(0xfff0f0f0)
    ) {
        when (color) {
            1 -> {
                val name = categoryList.map { it._1 }.last()
                TodoCategoryDetailHeader(color = color, categoryName = name!!)
            }
            2 -> {
                val name = categoryList.map { it._2 }.last()
                TodoCategoryDetailHeader(color = color, categoryName = name!!)
            }
            3 -> {
                val name = categoryList.map { it._3 }.last()
                TodoCategoryDetailHeader(color = color, categoryName = name!!)
            }
            4 -> {
                val name = categoryList.map { it._4 }.last()
                TodoCategoryDetailHeader(color = color, categoryName = name!!)
            }
            5 -> {
                val name = categoryList.map { it._5 }.last()
                TodoCategoryDetailHeader(color = color, categoryName = name!!)
            }
            6 -> {
                val name = categoryList.map { it._6 }.last()
                TodoCategoryDetailHeader(color = color, categoryName = name!!)
            }
        }
    }
}

@Composable
fun TodoCategoryDetailHeader(
    color: Int,
    categoryName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(9.dp)
                .clip(shape = CircleShape)
                .background(
                    color = when (color) {
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
            text = categoryName,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 17.sp
        )
    }
}