package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.todo_android.Component.CustomSwitch
import com.example.todo_android.Component.FloatingStateType
import com.example.todo_android.Component.TodoItemList
import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.Request.TodoRequest.CreateTodoRequest
import com.example.todo_android.Request.TodoRequest.ReadTodoRequest
import com.example.todo_android.Response.TodoResponse.CreateTodoResponse
import com.example.todo_android.Response.TodoResponse.RToDoResponse
import com.example.todo_android.Response.TodoResponse.ReadTodoResponse
import com.example.todo_android.Util.MyApplication
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.color.KalendarThemeColor
import com.himanshoe.kalendar.component.day.KalendarDay
import com.himanshoe.kalendar.component.day.config.KalendarDayColors
import com.himanshoe.kalendar.component.day.config.KalendarDayState
import com.himanshoe.kalendar.component.header.KalendarHeader
import com.himanshoe.kalendar.component.header.config.KalendarHeaderConfig
import com.himanshoe.kalendar.component.text.KalendarNormalText
import com.himanshoe.kalendar.component.text.config.KalendarTextColor
import com.himanshoe.kalendar.component.text.config.KalendarTextConfig
import com.himanshoe.kalendar.component.text.config.KalendarTextSize
import com.himanshoe.kalendar.model.KalendarDay
import com.himanshoe.kalendar.model.KalendarEvent
import com.himanshoe.kalendar.model.KalendarType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import androidx.compose.material3.TopAppBar as TopAppBar

fun goDetailProfile(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

fun createTodo(token: String, year: Int, month: Int, day: Int, title: String, time: String) {

    var createTodoResponse: CreateTodoResponse? = null

    var retrofit = Retrofit.Builder().baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    var createTodoRequest: CreateTodoRequest = retrofit.create(CreateTodoRequest::class.java)

    createTodoRequest.requestCreateTodo(token, CreateTodo(year, month, day, title, time))
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

                Log.d("createTodo", "token : " + MyApplication.prefs.getData("token", ""))
                Log.d("createTodo", "resultCode : " + createTodoResponse?.resultCode)
                Log.d("createTodo", "data : " + createTodoResponse?.data)

            }
        })
}

fun readTodo(
    token: String,
    year: Int,
    month: Int,
    day: Int,
    response: (ReadTodoResponse?) -> Unit,
) {

    var readTodoResponse: ReadTodoResponse? = null

    var retrofit = Retrofit.Builder().baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun CalendarScreen(routeAction: RouteAction) {

    val states = listOf("월간", "주간")
    var selectedOption by remember { mutableStateOf(states[0]) }

    val onSelectionChange = { text: String -> selectedOption = text }

    var isVisible by remember { mutableStateOf(false) }
    var isVisiblily by remember { mutableStateOf(false) }

    var multiFloatingState by remember {
        mutableStateOf(FloatingStateType.Collapsed)
    }

    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    var year by remember { mutableStateOf(0) }
    var month by remember { mutableStateOf(0) }
    var day by remember { mutableStateOf(0) }
    var title by remember { mutableStateOf("") }
    var time = "0900"

//    var done = true
//    var color = 0

    var todoList = remember {
        mutableStateListOf<RToDoResponse>()
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(
                        modifier = Modifier
                            .width(115.dp)
                            .height(35.dp)
                            .clip(shape = RoundedCornerShape(24.dp))
                            .background(Color(0xffe9e9ed))
                            .padding(start = 10.dp, end = 5.dp, top = 6.dp, bottom = 5.dp)
                    ) {
                        states.forEach { text ->
                            Text(text = text,
                                fontSize = 10.sp,
                                lineHeight = 16.sp,
                                color = if (text == selectedOption) {
                                    Color.Black
                                } else {
                                    Color.Gray
                                },
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(24.dp))
                                    .clickable {
                                        onSelectionChange(text)
                                        isVisible = (text == states[1])
//                                            isVisible = !isVisible
                                    }
                                    .background(
                                        if (text == selectedOption) {
                                            Color.White
                                        } else {
                                            Color(0xffe9e9ed)
                                        }
                                    )
                                    .padding(
                                        vertical = 5.dp,
                                        horizontal = 16.dp,
                                    ))
                        }
                    }
                }
            },
            actions = {
                IconButton(onClick = {
                    goDetailProfile(NAV_ROUTE.PROFILE, routeAction)
                }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "profile")
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black
            )
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xfff0f0f0))
                .imePadding()
        ) {
            if (isVisible) {
                Kalendar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 45.dp)
                        .clip(
                            shape = RoundedCornerShape(
                                bottomStart = 30.dp,
                                bottomEnd = 30.dp
                            )
                        ),
                    kalendarHeaderConfig = KalendarHeaderConfig(
                        kalendarTextConfig = KalendarTextConfig(
                            kalendarTextColor = KalendarTextColor(Color.Black),
                            kalendarTextSize = KalendarTextSize.SubTitle
                        )
                    ),
//                            kalendarEvents = List<KalendarDay> (
//                                size = ,
//                                init =
//                                    ),
                    kalendarType = KalendarType.Oceanic(),
                    kalendarDayColors = KalendarDayColors(Color.Black, Color.Black),
                    kalendarThemeColor = KalendarThemeColor(
                        backgroundColor = Color.White,
                        dayBackgroundColor = Color(0xffFBE3C7),
                        headerTextColor = Color.Black
                    ),
                    onCurrentDayClick = { kalendarDay: KalendarDay, kalendarEvents: List<KalendarEvent> ->

                        year = kalendarDay.localDate.year
                        month = kalendarDay.localDate.monthNumber
                        day = kalendarDay.localDate.dayOfMonth

                        readTodo(token, year, month, day, response = {

                            todoList.clear()
                            for (i in it!!.data) {
                                todoList.add(i)
                            }
                        })
                    })
            } else {
                Kalendar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp)
                        .clip(
                            shape = RoundedCornerShape(
                                bottomStart = 30.dp,
                                bottomEnd = 30.dp
                            )
                        ),
                    kalendarHeaderConfig = KalendarHeaderConfig(
                        kalendarTextConfig = KalendarTextConfig(
                            kalendarTextColor = KalendarTextColor(Color.Black),
                            kalendarTextSize = KalendarTextSize.SubTitle
                        )
                    ),
//                            com.himanshoe.kalendar.component.day.KalendarDay(kalendarDay =,
//                                selectedKalendarDay =,
//                                kalendarDayColors =,
//                                dotColor =,
//                                dayBackgroundColor =),
//                            kalendarEvents = List<KalendarEvent>(
//
//                            ),
                    kalendarType = KalendarType.Firey,
                    kalendarDayColors = KalendarDayColors(Color.Black, Color.Black),
                    kalendarThemeColor = KalendarThemeColor(
                        backgroundColor = Color.White,
                        dayBackgroundColor = Color(0xffFBE3C7),
                        headerTextColor = Color.Black
                    ),
                    onCurrentDayClick = { kalendarDay: KalendarDay, kalendarEvents: List<KalendarEvent> ->

                        year = kalendarDay.localDate.year
                        month = kalendarDay.localDate.monthNumber
                        day = kalendarDay.localDate.dayOfMonth

                        readTodo(token, year, month, day, response = {

                            todoList.clear()
                            for (i in it!!.data) {
                                todoList.add(i)
                            }
                        })
                    })
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 21.dp, end = 21.dp, top = 30.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = day.toString(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 5.dp)
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp),
                    color = Color(0xffD8D8D8)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                TodoItemList(Todo = todoList)
                
                AddTodoFloatingButton(
                    multiFloatingState = multiFloatingState,
                    onMultiFloatingStateChange = {
                        multiFloatingState = it
                    },
                    position = Modifier
                        .align(alignment = Alignment.BottomEnd)
                        .padding(all = 16.dp)
                )

            }
        }
    }
}

@Composable
fun AddTodoFloatingButton(
    multiFloatingState: FloatingStateType,
    onMultiFloatingStateChange: (FloatingStateType) -> Unit,
    position: Modifier
) {
    val transition = updateTransition(targetState = multiFloatingState, label = null)
    val rotate by transition.animateFloat(label = "rotate") {
        if (it == FloatingStateType.Expanded) {
            315f
        } else {
            0f
        }
    }

    FloatingActionButton(
//        modifier = Modifier
//            .align(alignment = Alignment.BottomEnd)
//            .padding(all = 16.dp),
        modifier = position,
        containerColor = Color(0xffFBE3C7),
        shape = CircleShape,
        onClick = {

            onMultiFloatingStateChange(
                if (transition.currentState == FloatingStateType.Expanded) {
                    FloatingStateType.Collapsed
                } else {
                    FloatingStateType.Expanded
                }
            )
//                        isVisiblily = !isVisiblily
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "todolist 추가",
            modifier = Modifier.rotate(rotate)
        )
    }
}


//                Row(
//                    modifier = Modifier.padding(12.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    TextField(
//                        modifier = Modifier
//                            .padding(12.dp)
//                            .width(250.dp)
//                            .height(50.dp),
//
//                        value = title,
//                        onValueChange = {
//                            title = it
//                        })
//
//                    Spacer(modifier = Modifier.width(10.dp))
//
//                    Button(
//                        modifier = Modifier.width(),
//                        onClick = {
//                            createTodo(token, year, month, day, title, time)
//                            isVisiblily = !isVisiblily
//                        }
//                    ) {
//                        Text(text = "Todo 작성")
//                    }
//                }
//
//
//


//    Column(
//        modifier = Modifier
//            .fillMaxSize()
////            .background(Color(0xfff0f0f0))
//            .background(Color.Red)
//            .imePadding(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Top
//    ) {
//
//        TopAppBar(
//            title = { Text(text = "asdasdasd") },
//            actions = {
//                IconButton(onClick = {
//                    goDetailProfile(NAV_ROUTE.PROFILE, routeAction)
//                }) {
//                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "profile")
//                }
//            }
//        )
//    }
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.White),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//
//            Row(
//                modifier = Modifier
//                    .clip(shape = RoundedCornerShape(24.dp))
//                    .background(Color(0xffe9e9ed))
//                    .padding(4.dp)
//            )
//            {
//                states.forEach { text ->
//                    Text(
//                        text = text,
//                        color =
//                        if (text == selectedOption) {
//                            Color.Black
//                        } else {
//                            Color.Gray
//                        },
//                        fontWeight = FontWeight.Medium,
//                        modifier = Modifier
//                            .clip(shape = RoundedCornerShape(24.dp))
//                            .clickable {
//                                onSelectionChange(text)
//                                isVisible = !isVisible
//                            }
//                            .background(
//                                if (text == selectedOption) {
//                                    Color.White
//                                } else {
//                                    Color(0xffe9e9ed)
//                                }
//                            )
//                            .padding(
//                                vertical = 5.dp,
//                                horizontal = 16.dp,
//                            )
//                    )
//                }
//            }
//        }
//        Spacer(modifier = Modifier.height(15.dp))

//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        AnimatedVisibility(isVisible)
//        {
//            Kalendar(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(260.dp),
//                kalendarType = KalendarType.Oceanic(),
//                kalendarDayColors = KalendarDayColors(Color.Black, Color.Black),
//                kalendarThemeColor = KalendarThemeColor(
//                    backgroundColor = Color.White,
//                    dayBackgroundColor = Color(0xffFBE3C7),
//                    headerTextColor = Color.Black),
//                onCurrentDayClick = { kalendarDay: KalendarDay, kalendarEvents: List<KalendarEvent> ->
//
//                    year = kalendarDay.localDate.year
//                    month = kalendarDay.localDate.monthNumber
//                    day = kalendarDay.localDate.dayOfMonth
//
//                    readTodo(token, year, month, day, response = {
//
//                        todoList.clear()
//                        for (i in it!!.data) {
//                            todoList.add(i)
//                        }
//                    })
//                })
//        }
//
//        AnimatedVisibility(!isVisible) {
//            Kalendar(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(428.dp)
//                    .padding(30.dp),
////                shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp),
//                kalendarType = KalendarType.Firey,
//                kalendarDayColors = KalendarDayColors(Color.Black, Color.Black),
//                kalendarThemeColor = KalendarThemeColor(
//                    backgroundColor = Color.White,
//                    dayBackgroundColor = Color(0xffFBE3C7),
//                    headerTextColor = Color.Black),
//                onCurrentDayClick = { kalendarDay: KalendarDay, kalendarEvents: List<KalendarEvent> ->
//
//                    year = kalendarDay.localDate.year
//                    month = kalendarDay.localDate.monthNumber
//                    day = kalendarDay.localDate.dayOfMonth
//
//                    readTodo(token, year, month, day, response = {
//
//                        todoList.clear()
//                        for (i in it!!.data) {
//                            todoList.add(i)
//                        }
//                    })
//                })
//        }
//
//        Spacer(modifier = Modifier.height(29.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            Text(
//                text = day.toString(),
//                modifier = Modifier.padding(12.dp)
//            )
//
//            Spacer(modifier = Modifier.width(5.dp))
//
//            Divider(modifier = Modifier.padding(5.dp), color = Color(0xffe7e7e7))
//
//        }
//
//        TodoItemList(Todo = todoList)
//
//        AnimatedVisibility(isVisiblily) {
//            Row(
//                modifier = Modifier.padding(12.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                TextField(
//                    modifier = Modifier
//                        .padding(12.dp)
//                        .width(250.dp)
//                        .height(50.dp),
//
//                    value = title,
//                    onValueChange = {
//                        title = it
//                    })
//
//                Spacer(modifier = Modifier.width(10.dp))
//
//                Button(
//                    onClick = {
//                        createTodo(token, year, month, day, title, time)
//                        isVisiblily = !isVisiblily
//                    }
//                ) {
//                    Text(text = "Todo 작성")
//                }
//            }
//        }
//
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(all = 25.dp)
//        ) {
//            FloatingActionButton(
//                modifier = Modifier.align(alignment = Alignment.BottomEnd),
//                shape = CircleShape,
//                onClick = {
//                    isVisiblily = !isVisiblily
//                }
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.Add,
//                    contentDescription = "todolist 추가"
//                )
//            }
//        }
//    }
//