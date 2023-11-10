package com.example.todo_android.ViewModel.ProFileViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthCodeViewModel: ViewModel() {

    // State Flow 초기값 설정
    private val _code = MutableStateFlow("")

    val code = _code.asStateFlow()

    // ViewModel이 가지고 있는 값을 변경하는 메소드
    fun inputCode(text: String) {
        _code.value = text
    }
}