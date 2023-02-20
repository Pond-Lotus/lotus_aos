package com.example.todo_android.Screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import com.himanshoe.kalendar.component.day.config.KalendarDayColors
import com.himanshoe.kalendar.model.KalendarDay
import com.himanshoe.kalendar.model.KalendarEvent
import com.himanshoe.kalendar.model.KalendarType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun goDetailProfile(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

fun createTodo(token: String, year: Int, month: Int, day: Int, title: String, time: String) {

    var createTodoResponse: CreateTodoResponse? = null

    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

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

    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

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


@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun CalendarScreen(routeAction: RouteAction) {

    val states = listOf(
        "월간",
        "주간"
    )
    var selectedOption by remember { mutableStateOf(states[1]) }

    val onSelectionChange = { text: String -> selectedOption = text }

    var isVisible by remember { mutableStateOf(false) }
    var isVisiblily by remember { mutableStateOf(false) }

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


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xfff0f0f0)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        TopAppBar(
            title = { Text(text = "") },
            actions = {
                IconButton(onClick = {
                    goDetailProfile(NAV_ROUTE.PROFILE, routeAction)
                }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "profile")
                }
            }
        )


        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(24.dp))
                .background(Color(0xffe9e9ed))
                .padding(4.dp)
        )
        {
            states.forEach { text ->
                Text(
                    text = text,
                    color =
                    if (text == selectedOption) {
                        Color.Black
                    } else {
                        Color.Gray
                    },
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(24.dp))
                        .clickable {
                            onSelectionChange(text)
                            isVisible = !isVisible
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
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(29.dp))

        AnimatedVisibility(isVisible)
        {
            Kalendar(
                modifier = Modifier.fillMaxWidth().height(260.dp).padding(30.dp),
                kalendarType = KalendarType.Oceanic(),
                kalendarDayColors = KalendarDayColors(Color.Black, Color.Black),
                kalendarThemeColor = KalendarThemeColor(
                    backgroundColor = Color.White,
                    dayBackgroundColor = Color(0xffFBE3C7),
                    headerTextColor = Color.Black),
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

        AnimatedVisibility(!isVisible) {
            Kalendar(
                modifier = Modifier.fillMaxWidth().height(428.dp).padding(30.dp),
//                shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp),
                kalendarType = KalendarType.Firey,
                kalendarDayColors = KalendarDayColors(Color.Black, Color.Black),
                kalendarThemeColor = KalendarThemeColor(
                    backgroundColor = Color.White,
                    dayBackgroundColor = Color(0xffFBE3C7),
                    headerTextColor = Color.Black),
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

        Spacer(modifier = Modifier.height(29.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = day.toString(),
                modifier = Modifier.padding(12.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Divider(modifier = Modifier.padding(5.dp), color = Color(0xffe7e7e7))

        }

        TodoItemList(Todo = todoList)

        AnimatedVisibility(isVisiblily) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    modifier = Modifier
                        .padding(12.dp)
                        .width(250.dp)
                        .height(50.dp),

                    value = title,
                    onValueChange = {
                        title = it
                    })

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = {
                        createTodo(token, year, month, day, title, time)
                        isVisiblily = !isVisiblily
                    }
                ) {
                    Text(text = "Todo 작성")
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 25.dp)
        ) {
            FloatingActionButton(
                modifier = Modifier.align(alignment = Alignment.BottomEnd),
                shape = CircleShape,
                onClick = {
                    isVisiblily = !isVisiblily
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "todolist 추가"
                )
            }
        }
    }
}