package com.example.todo_android.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todo_android.Response.TodoResponse.ReadTodoResponse
import java.time.MonthDay
import java.time.Year

@Composable
fun TodoItem(Todo: ReadTodoResponse) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(80.dp)
            .background(color = Color.White)

    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = Todo.data.toString(),
            )
        }
    }
}

@Composable
fun TodoItemList(Todo: List<ReadTodoResponse>) {
    LazyColumn {
        itemsIndexed(items = Todo) {
            index, item -> TodoItem(Todo = item) 
        }
    }
}