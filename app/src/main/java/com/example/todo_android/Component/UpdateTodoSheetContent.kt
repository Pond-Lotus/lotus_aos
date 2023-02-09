package com.example.todo_android.Component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun UpdateTodoSheetContent() {

    var description by remember { mutableStateOf("") }
    var time = "0900"
    val done: Int = 1

    Surface(
        modifier = Modifier.height(300.dp),
        color = Color.White
    ) {

        TopAppBar(
            title = { Text(text = "") },
            actions = {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "저장")
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    /*TODO */
                }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                }
            }
        )


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = description,
                onValueChange = {
                    description = it
                }
            )
        }
    }

}
