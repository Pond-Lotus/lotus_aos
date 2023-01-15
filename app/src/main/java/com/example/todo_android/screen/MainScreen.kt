package com.example.todo_android.screen


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@ExperimentalMaterial3Api
@Composable
fun MainScreen() {
//    LoginScreen()
//    AuthEmailScreen()
//    AuthCodeScreen()
    RegisterScreen()
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun MainScreenPreview() {
    MainScreen()
}