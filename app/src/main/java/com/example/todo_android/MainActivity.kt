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
//        loadData()
    }


//    fun loadData() {
//        val pref = getSharedPreferences("UserTokenKey", Context.MODE_PRIVATE) // UserTokenKey: 파일명
//        val token = pref.getString("Token", "")
//    }
//
//    fun saveData(token: String?) {
//        val pref = getSharedPreferences("UserTokenKey", Context.MODE_PRIVATE) // UserTokenKey: 파일명
//        val edit = pref.edit()
//        edit.putString("Token", token) // "Token"라는 이름을 사용하여 token값을 입력한다.
//        edit.commit()
//    }
}