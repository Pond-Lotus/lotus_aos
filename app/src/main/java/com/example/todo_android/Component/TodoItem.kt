package com.example.todo_android.Component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.todo_android.Request.TodoRequest.DeleteTodoRequest
import com.example.todo_android.Response.TodoResponse.DeleteTodoResponse
import com.example.todo_android.Response.TodoResponse.RToDoResponse
import com.example.todo_android.Util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.MonthDay
import java.time.Year

fun deleteTodo(
    token: String,
    id: Int
) {
    var deleteTodoResponse: DeleteTodoResponse? = null

    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var deleteTodoRequest: DeleteTodoRequest = retrofit.create(DeleteTodoRequest::class.java)

    deleteTodoRequest.requestDeleteTodo(token, id)
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


@Composable
fun TodoItem(Todo: RToDoResponse) {

    var checked by remember { mutableStateOf(false) }
    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    Card(
        modifier = Modifier
            .padding(12.dp)
            .width(334.dp)
            .height(60.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { deleteTodo(token, Todo.id) }
                )
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checked = it
                }
            )

            Spacer(modifier = Modifier.width(9.dp))

            Text(
                text = Todo.title
            )
        }
    }
}


@Composable
fun TodoItemList(Todo: List<RToDoResponse>) {
    LazyColumn {
        itemsIndexed(items = Todo) { index, item ->
            TodoItem(Todo = item)
        }
    }
}