package com.lotus.todo_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.constraintlayout.compose.ExperimentalMotionApi
import com.lotus.todo_android.fcm.MyFirebaseMessagingService
import com.lotus.todo_android.navigation.NavigationGraph
import com.lotus.todo_android.ui.theme.TodoandroidTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMotionApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoandroidTheme {
                NavigationGraph()
            }
        }

        // FCM 설정, 토큰 값 가져오기
        MyFirebaseMessagingService().getFirebaseToken()
    }
}