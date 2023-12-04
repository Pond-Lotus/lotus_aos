package com.example.todo_android.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.R
import com.example.todo_android.composable.*
import com.example.todo_android.navigation.Action.RouteAction
import com.example.todo_android.util.MyApplication
import com.example.todo_android.util.rememberFirstVisibleMonthAfterScroll
import com.example.todo_android.util.rememberFirstVisibleWeekAfterScroll
import com.example.todo_android.viewmodel.Todo.TodoViewModel
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

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
                vm.setTodoColor(1)
            }
            "2" -> {
                isVisibility = !isVisibility
                vm.setTodoColor(2)
            }
            "3" -> {
                isVisibility = !isVisibility
                vm.setTodoColor(3)
            }
            "4" -> {
                isVisibility = !isVisibility
                vm.setTodoColor(4)
            }
            "5" -> {
                isVisibility = !isVisibility
                vm.setTodoColor(5)
            }
            "6" -> {
                isVisibility = !isVisibility
                vm.setTodoColor(6)
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

//    LaunchedEffect(isVisibility) {
//        if (isVisibility) {
//            focusRequester.requestFocus()
//        }
//    }

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
            if(!isVisibility || bottomScaffoldState.bottomSheetState.isExpanded){
                AddTodoFloatingButton(
                    multiFloatingState = multiFloatingState,
                    onMultiFloatingStateChange = { multiFloatingState = it },
                    backgroundColor = colorFAB,
                    onButtonClick = onButtonClick,
//                focusRequester = focusRequester
                )
            }
        },
        floatingActionButtonPosition = androidx.compose.material.FabPosition.End,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(
            topStart = 20.dp, topEnd = 20.dp
        ),
        sheetContent = {
            TodoUpdateBottomSheet(
                vm = vm,
                scope = scope,
                bottomSheetScaffoldState = bottomScaffoldState,
                focusRequester = focusRequester
            )
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
                            vm = vm,
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
                scope = scope,
                bottomScaffoldState = bottomScaffoldState
            )

            item {
                val focusRequester = remember { FocusRequester() }

                LaunchedEffect(isVisibility) {
                    if (isVisibility) {
                        focusRequester.requestFocus()
                    }
                }

                if (isVisibility) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .padding(start = 21.dp, end = 21.dp),
                        colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(
                                    start = 14.dp,
                                    top = 13.dp,
                                    bottom = 13.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = R.drawable.defaultcheckbox),
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
                                onValueChange = { text ->
                                    vm.setTodoTitle(text)
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