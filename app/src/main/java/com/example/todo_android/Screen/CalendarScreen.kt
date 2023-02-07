package com.example.todo_android.Screen

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todo_android.Component.TodoItemList
import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.Request.TodoRequest.CreateTodoRequest
import com.example.todo_android.Request.TodoRequest.DeleteTodoRequest
import com.example.todo_android.Request.TodoRequest.ReadTodoRequest
import com.example.todo_android.Request.TodoRequest.UpdateTodoRequest
import com.example.todo_android.Response.TodoResponse.*
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


fun createTodo(token: String, year: String, month: String, day: String, title: String) {

    var createTodoResponse: CreateTodoResponse? = null

    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var createTodoRequest: CreateTodoRequest = retrofit.create(CreateTodoRequest::class.java)

    createTodoRequest.requestCreateTodo(token, CreateTodo(year, month, day, title))
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

fun updateTodo(
    token: String,
    year: String,
    month: String,
    day: String,
    title: String,
    done: String,
) {

    var updateTodoResponse: UpdateTodoResponse? = null

    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var updateTodoRequest: UpdateTodoRequest = retrofit.create(UpdateTodoRequest::class.java)

    updateTodoRequest.requestUpdateTodo(token, UpdateTodo(year, month, day, title, done))
        .enqueue(object : Callback<UpdateTodoResponse> {

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

                    Log.d("updateTodo", "token : " + MyApplication.prefs.getData("token", ""))
                    Log.d("updateTodo", "resultCode : " + updateTodoResponse?.resultCode)
                    Log.d("updateTodo", "data : " + updateTodoResponse?.data)
                } else {
                    Log.e("updateTodo", "resultCode : " + response.body())
                    Log.e("updateTodo", "code : " + response.code())
                }
            }
        })
}

fun deleteTodo(
    token: String,
) {
    var deleteTodoResponse: DeleteTodoResponse? = null

    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var deleteTodoRequest: DeleteTodoRequest = retrofit.create(DeleteTodoRequest::class.java)

    deleteTodoRequest.requestDeleteTodo(token)
        .enqueue(object : Callback<DeleteTodoResponse> {

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

                Log.d("deleteTodo", "token : " + MyApplication.prefs.getData("token", ""))
                Log.d("deleteTodo", "resultCode : " + deleteTodoResponse?.resultCode)
                Log.d("deleteTodo", "data : " + deleteTodoResponse?.data)
            }
        })
}


@ExperimentalMaterial3Api
@Composable
fun CalendarScreen(routeAction: RouteAction) {

    val states = listOf(
        "월간",
        "주간"
    )
    var selectedOption by remember { mutableStateOf(states[1]) }

    val onSelectionChange = { text: String -> selectedOption = text }

    var isVisible by remember { mutableStateOf(true) }

    val token = "Token ${MyApplication.prefs.getData("token", "")}"

//    val year = "2023"
//    val month = "2"
//    val day = "6"
//    val token = "Token ${MyApplication.prefs.getData("token", "")}"
//    val title = "qkrwhdwns"
//    val done = "true"

    var year by remember { mutableStateOf(0) }
    var month by remember { mutableStateOf(0) }
    var day by remember { mutableStateOf(0) }
    val title = remember { mutableStateOf("") }
    val done = remember { mutableStateOf("") }

    var todoList = remember {
        mutableStateListOf<RToDoResponse>()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
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

        Text(text = day.toString())

        TodoItemList(Todo = todoList)

        Box(
            modifier = Modifier.fillMaxSize().padding(all = 25.dp)
        ) {
            FloatingActionButton(
                modifier = Modifier.align(alignment = Alignment.BottomEnd),
                shape = CircleShape,
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "todolist 추가"
                )
            }
        }

//        Surface(
//            shape = RoundedCornerShape(24.dp),
//            modifier = Modifier
//                .wrapContentSize()
//        ) {}


//        Button(
//            modifier = Modifier
//                .width(300.dp)
//                .height(50.dp),
//            colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
//            onClick = { readTodo(token, year, month, day) }
//        ) {
//            Text(text = "TODO 조회", color = Color.Black)
//        }
//
//        Spacer(modifier = Modifier.height(30.dp))
//
//        Button(
//            modifier = Modifier
//                .width(300.dp)
//                .height(50.dp),
//            colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
//            onClick = { createTodo(token, year, month, day, title) }
//        ) {
//            Text(text = "TODO 작성", color = Color.Black)
//        }
//
//        Spacer(modifier = Modifier.height(30.dp))
//
//        Button(
//            modifier = Modifier
//                .width(300.dp)
//                .height(50.dp),
//            colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
//            onClick = { updateTodo(token, year, month, day, title, done) }
//        ) {
//            Text(text = "TODO 수정", color = Color.Black)
//        }
//
//        Spacer(modifier = Modifier.height(30.dp))
//
//        Button(
//            modifier = Modifier
//                .width(300.dp)
//                .height(50.dp),
//            colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
//            onClick = { deleteTodo(token) }
//        ) {
//            Text(text = "TODO 삭제", color = Color.Black)
//        }
    }
}