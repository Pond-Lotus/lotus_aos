package com.example.todo_android.Util

import android.content.Context
import android.content.SharedPreferences

class TokenUtil(context: Context) {

    val token: SharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)

    fun getToken(key: String, value: String): String {
        return token.getString(key, value).toString()
    }

    fun setToken(key: String, value: String) {
        token.edit().putString(key, value).apply()
    }
}