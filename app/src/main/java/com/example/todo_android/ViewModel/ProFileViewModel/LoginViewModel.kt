package com.example.todo_android.ViewModel.ProFileViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// 데이터의 변경
// ViewModel은 데이터의 변경사항을 알려주는 live data를 가지고 있다.
class LoginViewModel : ViewModel() {

    // State Flow 초기값 설정
    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")

    //
    val email = _email.asStateFlow()
    val password = _password.asStateFlow()

    // ViewModel이 가지고 있는 값을 변경하는 메소드
    fun inputEmail(text: String) {
        _email.value = text
    }

    fun inputPassword(text: String) {
        _password.value = text
    }
}