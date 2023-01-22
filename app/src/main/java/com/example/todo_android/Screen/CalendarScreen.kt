package com.example.todo_android.Screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todo_android.Data.Todo.CreateTodo
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Request.TodoRequest.CreateTodoRequest
import com.example.todo_android.Request.TodoRequest.ReadTodoRequest
import com.example.todo_android.Response.TodoResponse.CreateTodoResponse
import com.example.todo_android.Response.TodoResponse.ReadTodoResponse
import com.example.todo_android.Util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

fun readTodo(token: String, year: String, month: String, day: String) {

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
    done: Boolean,
) {

}

fun deleteTodo(
    token: String,
    year: String,
    month: String,
    day: String,
    title: String,
    done: Boolean,
) {

}


@Composable
fun CalendarScreen(routeAction: RouteAction) {

    val states = listOf(
        "월간",
        "주간"
    )
    var selectedOption by remember { mutableStateOf(states[1]) }

    val onSelectionChange = { text: String -> selectedOption = text }

    val year = "2023"
    val month = "1"
    val day = "30"
    val token = "Token ${MyApplication.prefs.getData("token", "")}"
    val title = "1/22일 입력"
    val done = false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

//        Spacer(modifier = Modifier.height(52.dp))
//
//        Row(
//            modifier = Modifier
//                .clip(shape = RoundedCornerShape(24.dp))
//                .background(Color(0xffe9e9ed))
//        )
//        {
//            states.forEach { text ->
//                Text(
//                    text = text,
//                    color =
//                    if (text == selectedOption) {
//                        Color.Black
//                    } else {
//                        Color.Gray
//                    },
//                    modifier = Modifier
//                        .clip(shape = RoundedCornerShape(24.dp))
//                        .clickable {
//                            onSelectionChange(text)
//                        }
//                        .background(
//                            Color.LightGray
//                        )
//                        .padding(
//                            vertical = 12.dp,
//                            horizontal = 16.dp,
//                        ),
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(73.dp))
//
//
//        // 가로 스크롤 커스텀 캘린더
//        Kalendar(kalendarType = KalendarType.Oceanic())
//        // 기본 커스텀 캘린더
//        Kalendar(kalendarType = KalendarType.Firey)


//        Surface(
//            shape = RoundedCornerShape(24.dp),
//            modifier = Modifier
//                .wrapContentSize()
//        ) {}


        Button(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
            onClick = { readTodo(token, year, month, day) }
        ) {
            Text(text = "TODO 조회", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
            onClick = { createTodo(token, year, month, day, title) }
        ) {
            Text(text = "TODO 작성", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
            onClick = { updateTodo(token, year, month, day, title, done) }
        ) {
            Text(text = "TODO 수정", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
            onClick = { deleteTodo(token, year, month, day, title, done) }
        ) {
            Text(text = "TODO 삭제", color = Color.Black)
        }
    }
}
