package com.example.todo_android.Component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.Request.TodoRequest.UpdateTodoRequest
import com.example.todo_android.Response.TodoResponse.UpdateTodoResponse
import com.example.todo_android.Util.MyApplication
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun updateTodo(
    token: String,
    year: Int,
    month: Int,
    day: Int,
    title: String,
    done: Int,
    description: String,
    color: Int,
    time: String,
    id: Int
) {

    var updateTodoResponse: UpdateTodoResponse? = null

    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var updateTodoRequest: UpdateTodoRequest = retrofit.create(UpdateTodoRequest::class.java)

    updateTodoRequest.requestUpdateTodo(token, id, UpdateTodo(year, month, day, title, done, description, color, time))
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

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun UpdateTodoSheet(
    year: Int,
    month: Int,
    day: Int,
    done: Int,
    color: Int,
    id: Int,
    title: String,
    time: String,
) {

    var description by remember { mutableStateOf("") }
    val token = "Token ${MyApplication.prefs.getData("token", "")}"


    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))

    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .background(Color.White)
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    TopAppBar(
                        title = { Text(text = "") },
                        navigationIcon = {
//                            androidx.compose.material3.IconButton(onClick = {
//                                routeAction.goBack()
//                            }) {
//                                Icon(imageVector = Icons.Filled.ArrowBack,
//                                    contentDescription = "back")
//                            }

                            IconButton(onClick = {
                                /**/
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "close"
                                )
                            }
                        },
                        actions = {

                            Button(
                                onClick = {
                                    updateTodo(
                                        token,
                                        year,
                                        month,
                                        day,
                                        title,
                                        done,
                                        description,
                                        color,
                                        time,
                                        id
                                    )
                                },
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .width(45.dp)
                                    .height(25.dp)
                            ) {
                                Text(
                                    text = "저장",
                                    modifier = Modifier.padding(6.dp))
                            }
                        }
                    )


                    Text(
                        text = "${month} 월 ${day}일",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold)

                    Divider()

                    TextField(
                        modifier = Modifier
                            .width(340.dp)
                            .height(65.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0xffF3F3F3),
                            disabledLabelColor = Color(0xffF3F3F3),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        shape = RoundedCornerShape(10.dp),
                        placeholder = {
                            Text(
                                text = "수정할 텍스트 입력",
                                fontSize = 16.sp,
                                color = Color(0xffA9A9A9)
                            )
                        },
                        value = description,
                        onValueChange = {
                            description = it
                        }
                    )
                }
            }
        }, sheetPeekHeight = 0.dp
    ) {

        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(onClick = {

                coroutineScope.launch {

                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    } else {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }

            }) {
                Text(text = "Click Me", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}