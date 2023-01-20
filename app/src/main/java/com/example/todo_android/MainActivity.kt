package com.example.todo_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.todo_android.Navigation.NavigationGraph
import com.example.todo_android.ui.theme.TodoandroidTheme

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoandroidTheme {
                NavigationGraph()
            }
        }
    }
}