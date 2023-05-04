package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.DismissDirection
import androidx.compose.material.DrawerValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import com.example.todo_android.Component.*
import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.TodoRequest.CreateTodoRequest
import com.example.todo_android.Request.TodoRequest.DeleteTodoRequest
import com.example.todo_android.Request.TodoRequest.ReadTodoRequest
import com.example.todo_android.Request.TodoRequest.UpdateTodoRequest
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun goDetailProfile(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

fun createTodo(
    token: String,
    year: Int,
    month: Int,
    day: Int,
    title: String,
    color: String,
    response: (CreateTodoResponse?) -> Unit,
) {

    var createTodoResponse: CreateTodoResponse? = null

    var retrofit = Retrofit.Builder().baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
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

fun deleteTodo(
    token: String,
    id: Int,
    response: (DeleteTodoResponse?) -> Unit,
) {
    var deleteTodoResponse: DeleteTodoResponse? = null

    var retrofit = Retrofit.Builder().baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
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
    year: Int,
    month: Int,
    day: Int,
    title: String,
    done: Boolean,
    description: String,
    color: Int,
    time: String,
    id: Int,
) {

    var updateTodoResponse: UpdateTodoResponse? = null

    var retrofit = Retrofit.Builder().baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    var updateTodoRequest: UpdateTodoRequest = retrofit.create(UpdateTodoRequest::class.java)

    updateTodoRequest.requestUpdateTodo(token,
        id,
        UpdateTodo(year, month, day, title, done, description, color, time))
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

@ExperimentalComposeUiApi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun CalendarScreen(routeAction: RouteAction) {

    val states = listOf("월간", "주간")
    var selectedOption by remember { mutableStateOf(states[0]) }

    val onSelectionChange = { text: String -> selectedOption = text }

    var selectCalendar by remember { mutableStateOf(false) }
    var showTodoInput by remember { mutableStateOf(false) }
    var isVisibility by remember { mutableStateOf(false) }

    var multiFloatingState by remember { mutableStateOf(FloatingStateType.Collapsed) }

    val colorFAB = if (multiFloatingState == FloatingStateType.Expanded) {
        Color(0xff9E9E9E)
    } else {
        Color(0xffFFDAB9)
    }


    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    var year by remember { mutableStateOf(0) }
    var month by remember { mutableStateOf(0) }
    var day by remember { mutableStateOf(0) }
    var title by remember { mutableStateOf("") }
    var time = "0000"

    var color by remember { mutableStateOf("0") }

    var todoList = remember { mutableStateListOf<RToDoResponse>() }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val bottomScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))
    val scrollState = rememberScrollState()

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
    BottomSheetScaffold(
        scaffoldState = bottomScaffoldState,
        drawerContent = {
            ProfileModalDrawer(
                scope = scope,
                bottomScaffoldState = bottomScaffoldState
            )
        }, topBar = {
            CenterAlignedTopAppBar(title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(modifier = Modifier
                        .width(115.dp)
                        .height(35.dp)
                        .clip(shape = RoundedCornerShape(24.dp))
                        .background(Color(0xffe9e9ed))
                        .padding(start = 8.dp, end = 8.dp, top = 5.dp, bottom = 5.dp)) {
                        states.forEach { text ->
                            Text(text = text,
                                fontSize = 10.sp,
                                lineHeight = 14.sp,
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
                                        selectCalendar = (text == states[1])
                                    }
                                    .background(if (text == selectedOption) {
                                        Color.White
                                    } else {
                                        Color(0xffe9e9ed)
                                    })
                                    .padding(
                                        vertical = 6.dp,
                                        horizontal = 16.dp,
                                    ))
                        }
                    }
                }
            },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            bottomScaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White,
                    titleContentColor = Color.Black))
        }, floatingActionButton = {
            AddTodoFloatingButton(multiFloatingState = multiFloatingState,
                onMultiFloatingStateChange = {
                    multiFloatingState = it
                },
                backgroundColor = colorFAB,
                onButtonClick = onButtonClick)
        }, floatingActionButtonPosition = androidx.compose.material.FabPosition.End,
        sheetContent = {
            TodoUpdateBottomSheet(scope, bottomScaffoldState)
        }, sheetPeekHeight = 0.dp, sheetShape = RoundedCornerShape(20.dp)

    ) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xfff0f0f0))
            .imePadding()
//                .horizontalScroll(scrollState)
//                .verticalScroll(scrollState)
//                .scrollable(state = scrollState, orientation = Orientation.Horizontal)
        ) {
            if (selectCalendar) {
                Kalendar(modifier = Modifier
                    .fillMaxWidth()
                    .shadow(shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp),
                        elevation = 3.dp),
                    kalendarHeaderConfig = KalendarHeaderConfig(kalendarTextConfig = KalendarTextConfig(
                        kalendarTextColor = KalendarTextColor(Color.Black),
                        kalendarTextSize = KalendarTextSize.SubTitle)),
                    kalendarType = KalendarType.Oceanic(),
                    kalendarDayColors = KalendarDayColors(Color.Black, Color.Black),
                    kalendarThemeColor = KalendarThemeColor(backgroundColor = Color.White,
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
            } else {
                Kalendar(modifier = Modifier
                    .fillMaxWidth()
                    .shadow(shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp),
                        elevation = 3.dp),
                    kalendarHeaderConfig = KalendarHeaderConfig(kalendarTextConfig = KalendarTextConfig(
                        kalendarTextColor = KalendarTextColor(Color.Black),
                        kalendarTextSize = KalendarTextSize.SubTitle)),
                    kalendarType = KalendarType.Firey,
                    kalendarDayColors = KalendarDayColors(Color.Black, Color.Black),
                    kalendarThemeColor = KalendarThemeColor(backgroundColor = Color.White,
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
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 21.dp, end = 21.dp, top = 30.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = day.toString(),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 5.dp))

                    Divider(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp),
                        color = Color(0xffD8D8D8))
                }

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp, start = 21.dp, end = 21.dp)) {

                    if (showTodoInput) {
                        TextField(modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .focusRequester(focusRequester),
                            value = title,
                            colors = TextFieldDefaults.textFieldColors(containerColor = Color(
                                0xffD8D8D8),
                                disabledLabelColor = Color(0xffD8D8D8),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            onValueChange = {
                                title = it
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                createTodo(token, year, month, day, title, color, response = {
                                    readTodo(token,
                                        year = year,
                                        month = month,
                                        day = day,
                                        response = {
                                            todoList.clear()
                                            for (i in it!!.data) {
                                                todoList.add(i)
                                            }
                                        })
                                })
                                keyboardController?.hide()
                                title = ""
                                showTodoInput = !showTodoInput
                            }))
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    TodoItemList(Todo = todoList, todoList = todoList, onTodoItemClick = { todo ->
                        scope.launch {
                            bottomScaffoldState.bottomSheetState.expand()
                        }
                    })
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
    val rotate by transition.animateFloat(label = "rotate") {
        if (it == FloatingStateType.Expanded) {
            315f
        } else {
            0f
        }
    }
    Column(horizontalAlignment = Alignment.End) {
        if (transition.currentState == FloatingStateType.Expanded) {
            FloatingActionButtonMenus(onMultiFloatingStateChange, onButtonClick)
        }

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        FloatingActionButton(containerColor = backgroundColor, shape = CircleShape, onClick = {
            onMultiFloatingStateChange(if (transition.currentState == FloatingStateType.Expanded) {
                FloatingStateType.Collapsed
            } else {
                FloatingStateType.Expanded
            })
        }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "todolist 추가",
                modifier = Modifier.rotate(rotate),
            )
        }
    }
}

@Composable
fun FloatingActionButtonMenus(
    onMultiFloatingStateChange: (FloatingStateType) -> Unit,
    onButtonClick: (String) -> Unit,
) {
    Surface(modifier = Modifier
        .width(150.dp)
        .height(110.dp)
        .shadow(shape = RoundedCornerShape(20.dp), elevation = 15.dp)
        .background(Color.White)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffFFB4B4)),
                    onClick = {
                        onButtonClick("1")
                        onMultiFloatingStateChange(FloatingStateType.Collapsed)
                    }, content = {})
                Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffFFDCA8)),
                    onClick = {
                        onButtonClick("2")
                        onMultiFloatingStateChange(FloatingStateType.Collapsed)
                    }, content = {})
                Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffB1E0CF)),
                    onClick = {
                        onButtonClick("3")
                        onMultiFloatingStateChange(FloatingStateType.Collapsed)
                    }, content = {})
            }

            Spacer(modifier = Modifier.padding(vertical = 7.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffB7D7F5)),
                    onClick = {
                        onButtonClick("4")
                        onMultiFloatingStateChange(FloatingStateType.Collapsed)
                    }, content = {})
                Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffFFB8EB)),
                    onClick = {
                        onButtonClick("5")
                        onMultiFloatingStateChange(FloatingStateType.Collapsed)
                    }, content = {})
                Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffB6B1EC)),
                    onClick = {
                        onButtonClick("6")
                        onMultiFloatingStateChange(FloatingStateType.Collapsed)
                    }, content = {})
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Composable
fun TodoItem(Todo: RToDoResponse, onTodoItemClick: (RToDoResponse) -> Unit) {

    var checked by remember { mutableStateOf(false) }
    val token = "Token ${MyApplication.prefs.getData("token", "")}"
    var time = "0900"
    var done = true
    var color = 0

    Card(colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(350.dp)
            .height(50.dp)
            .clickable {
                onTodoItemClick(Todo)
                Log.d("onclick", "onClick: ${Todo.id}")
            }) {
        Row(modifier = Modifier.padding(start = 13.dp, top = 15.dp, bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Checkbox(checked = checked, onCheckedChange = {
                checked = it
            })
            Text(text = Todo.title, fontSize = 13.sp, fontStyle = FontStyle.Normal)
        }
    }
}

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

    LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {

        items(items = Todo, key = { Todo -> Todo.id }) { item ->

            val dismissState = androidx.compose.material.rememberDismissState()
            val dismissDirection = dismissState.dismissDirection
            val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
            if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
                deleteTodo(token, item.id, response = {
                    todoList.remove(item)
                    readTodo(token,
                        year = item.year,
                        month = item.month,
                        day = item.day,
                        response = {
                            todoList.clear()
                            for (i in it!!.data) {
                                todoList.add(i)
                            }
                        })
                })
            }

            androidx.compose.material.SwipeToDismiss(state = dismissState,
                background = { DeleteBackground() },
                directions = setOf(DismissDirection.EndToStart),
                dismissContent = {
                    TodoItem(Todo = item, onTodoItemClick = onTodoItemClick)
                },
                dismissThresholds = {
                    androidx.compose.material.FractionalThreshold(fraction = 0.2f)
                })
        }
    }
}

@Composable
fun DeleteBackground() {
    Box(modifier = Modifier
        .width(350.dp)
        .height(50.dp)
        .clip(shape = RoundedCornerShape(8.dp))
        .background(deleteBackground)
        .padding(horizontal = 24.dp),
        contentAlignment = Alignment.CenterEnd) {
        Text(text = "삭제", color = Color.White)
    }
}

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun TodoUpdateBottomSheet(
    scope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    Todo: RToDoResponse,
) {

    var text by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .height(380.dp)
        .padding(start = 25.dp, end = 25.dp, top = 35.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 17.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            androidx.compose.material.IconButton(

                onClick = {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = null
                )
            }
            Button(modifier = Modifier
                .width(70.dp)
                .height(30.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
                onClick = {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                },
                shape = RoundedCornerShape(20.dp)) {
                Text(text = "저장",
                    color = Color.Black,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal)
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Button(modifier = Modifier
                .width(9.dp)
                .height(51.dp),
                onClick = { /*TODO*/ },
                enabled = false, content = {})
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start) {

                Text(text = "test",
                    fontSize = 15.sp,
                    lineHeight = 19.sp)
//                Text(
//                    text = "${Todo.month}" + "월" + "${Todo.day}" + "일",
//                    fontSize = 15.sp,
//                    lineHeight = 19.sp)
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                Text(text = "test",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 31.sp)
//                Text(
//                    text = Todo.description,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    lineHeight = 31.sp)
            }
        }

        TextField(modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(bottom = 22.dp),
            value = text,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xffF2F2F2),
                disabledLabelColor = Color(0xffF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(10.dp),
            onValueChange = {
                text = it
            })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 22.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.clock),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 11.dp))
            Text(text = "시간",
                modifier = Modifier.padding(end = 10.dp),
                fontWeight = FontWeight.Bold,
                lineHeight = 19.sp,
                fontSize = 19.sp)
            Text(text = "시간",
                lineHeight = 19.sp,
                fontSize = 19.sp)
        }

        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, bottom = 19.dp),
            color = Color(0xffe9e9e9))

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffFFB4B4)),
                onClick = {
                }, content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffFFDCA8)),
                onClick = {
                }, content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffB1E0CF)),
                onClick = {
                }, content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffB7D7F5)),
                onClick = {
                }, content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffFFB8EB)),
                onClick = {
                }, content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffB6B1EC)),
                onClick = {
                }, content = {})
        }
    }
}
