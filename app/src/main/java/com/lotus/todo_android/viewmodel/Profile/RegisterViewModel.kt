package com.lotus.todo_android.viewmodel.Profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel : ViewModel() {

    // State Flow 초기값 설정
    private val _nickname = MutableStateFlow("")
    private val _password1 = MutableStateFlow("")
    private val _password2 = MutableStateFlow("")


    val nickname = _nickname.asStateFlow()
    val password1 = _password1.asStateFlow()
    val password2 = _password2.asStateFlow()

    // ViewModel이 가지고 있는 값을 변경하는 메소드
    fun inputNickName(text: String) {
        _nickname.value = text
    }

    fun inputPassWord1(text: String) {
        _password1.value = text
    }

    fun inputPassWord2(text: String) {
        _password2.value = text
    }
}