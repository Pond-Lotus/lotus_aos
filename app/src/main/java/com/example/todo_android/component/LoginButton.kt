package com.example.todo_android.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun LoginButton() {
    Button(
        modifier = Modifier
            .width(300.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
        onClick = { /* */ }
    ) {
        Text(text = "로그인", color = Color.Black)
    }
}
@ExperimentalMaterial3Api
@Composable
fun LoginButtonContent() {
    LoginButton()
}

@ExperimentalMaterial3Api

@Composable
@Preview
fun LoginButtonPreview() {
    LoginButtonContent()
}