package com.example.todo_android.Component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_android.R

@Composable
fun RegisterButton() {
    Button(
        modifier = Modifier
            .width(300.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
        onClick = { /*TODO*/ }
    ) {
        Text(text = stringResource(id = R.string.register), color = Color.Black)

    }
}

@Composable
fun RegisterButtonContent() {
    RegisterButton()
}

@Composable
@Preview
fun RegisterButtonPreview() {
    RegisterButtonContent()
}