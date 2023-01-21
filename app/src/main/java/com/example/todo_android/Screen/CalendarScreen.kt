package com.example.todo_android.Screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Request.TodoRequest.ReadTodoRequest
import com.example.todo_android.Response.TodoResponse.ReadTodoResponse
import com.example.todo_android.Util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getData(token: String, year: String, month: String, day: String) {

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
                Log.e("CALENDAR", t.message.toString())
            }

            //성공할 경우
            override fun onResponse(
                call: Call<ReadTodoResponse>,
                response: Response<ReadTodoResponse>,
            ) {
                readTodoResponse = response.body()

                Log.d("CALENDAR1", "resultCode : " + readTodoResponse?.resultCode)
                Log.d("CALENDAR1", "data : " + readTodoResponse?.data)
//                Log.d("CALENDAR1", "data : " + readTodoResponse?.data)
            }
        })
}

@Composable
fun CalendarScreen(routeAction: RouteAction) {

//    val states = listOf(
//        "월간",
//        "주간"
//    )
//    var selectedOption by remember { mutableStateOf(states[1]) }
//
//    val onSelectionChange = { text: String -> selectedOption = text }

    val year = "2023"
    val month = "1"
    val day = "5"
    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
            onClick = { getData(token, year, month, day) }
        ) {
            Text(text = "투두리스트 값 가져오기", color = Color.Black)
        }


//        Surface(
//            shape = RoundedCornerShape(24.dp),
//            modifier = Modifier
//                .wrapContentSize()
//        ) {
//            Row(
//                modifier = Modifier
//                    .clip(shape = RoundedCornerShape(24.dp))
//                    .background(Color.LightGray)
//            ) {
//                states.forEach { text ->
//                    Text(
//                        text = text,
//                        color =
//                        if (text == selectedOption) {
//                            Color.Black
//                        } else {
//                            Color.Gray
//                        },
//                        modifier = Modifier
//                            .clip(shape = RoundedCornerShape(24.dp))
//                            .clickable {
//                                onSelectionChange(text)
//                            }
//                            .background(
//                                Color.LightGray
//                            )
//                            .padding(
//                                vertical = 12.dp,
//                                horizontal = 16.dp,
//                            ),
//                    )
//                }
//            }
//            // 가로 스크롤 커스텀 캘린더
//            Kalendar(kalendarType = KalendarType.Oceanic())
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // 기본 커스텀 캘린더
//            Kalendar(kalendarType = KalendarType.Firey)
    }
}
