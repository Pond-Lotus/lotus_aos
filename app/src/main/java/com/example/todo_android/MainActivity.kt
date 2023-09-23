package com.example.todo_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.constraintlayout.compose.ExperimentalMotionApi
import com.example.todo_android.FCM.MyFirebaseMessagingService
import com.example.todo_android.Navigation.NavigationGraph
import com.example.todo_android.ui.theme.TodoandroidTheme

@ExperimentalMotionApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TodoandroidTheme {
                NavigationGraph()
            }
        }

        // FCM 설정, 토큰 값 가져오기
        MyFirebaseMessagingService().getFirebaseToken()
    }
}