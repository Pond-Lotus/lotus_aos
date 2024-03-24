package com.lotus.todo_android.viewmodel.Profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthEmailViewModel: ViewModel() {

    // State Flow 초기값 설정
    private val _email = MutableStateFlow("")


    val email = _email.asStateFlow()

    // ViewModel이 가지고 있는 값을 변경하는 메소드
    fun inputEmail(text: String) {
        _email.value = text
    }
}