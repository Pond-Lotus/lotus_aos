package com.example.todo_android.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todo_android.Navigation.Action.RouteAction


@Composable
fun CalendarScreen(routeAction: RouteAction) {

    val states = listOf(
        "월간",
        "주간"
    )
    var selectedOption by remember { mutableStateOf(states[1]) }

    val onSelectionChange = { text: String -> selectedOption = text }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Surface(
            shape = RoundedCornerShape(24.dp),
//            elevation = 4.dp,
            modifier = Modifier
                .wrapContentSize()
        ) {
            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(Color.LightGray)
            ) {
                states.forEach { text ->
                    Text(
                        text = text,
                        color =
                        if (text == selectedOption) {
                            Color.Magenta
                        } else {
                            Color.Black
                        },
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(24.dp))
                            .clickable {
                                onSelectionChange(text)
                            }
                            .background(
//                                Color.LightGray
                                if (text == selectedOption) {
                                    Color.Green
                                } else {
                                    Color.LightGray
                                }
                            )
                            .padding(
                                vertical = 12.dp,
                                horizontal = 16.dp,
                            ),
                    )
                }
            }
//            // 가로 스크롤 커스텀 캘린더
//            Kalendar(kalendarType = KalendarType.Oceanic())
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // 기본 커스텀 캘린더
//            Kalendar(kalendarType = KalendarType.Firey)
        }
    }
}