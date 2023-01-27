package com.example.todo_android.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.Month
import java.time.MonthDay
import java.time.Year

@Composable
fun TodoItem(number: Int) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .border(width = 2.dp, color = Color.LightGray)
            .fillMaxWidth()
            .height(80.dp)
            .background(color = Color.White),

        ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "체크리스트 입니다 ${number}",
            )
        }
    }
}