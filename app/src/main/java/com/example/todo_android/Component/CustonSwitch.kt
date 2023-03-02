package com.example.todo_android.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomSwitch() {
    val states = listOf("월간", "주간")
    var selectedOption by remember { mutableStateOf(states[1]) }
    val onSelectionChange = { text: String -> selectedOption = text }
    var isVisible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .width(115.dp)
            .height(35.dp)
            .clip(shape = RoundedCornerShape(24.dp))
            .background(Color(0xffe9e9ed))
            .padding(4.dp)
    )
    {
        states.forEach { text ->
            Text(
                text = text,
                fontSize = 10.sp,
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
}

