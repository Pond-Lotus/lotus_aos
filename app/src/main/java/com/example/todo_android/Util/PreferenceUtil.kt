package com.example.todo_android.Util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {


    val token: SharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)
    val email: SharedPreferences = context.getSharedPreferences("email", Context.MODE_PRIVATE)


    fun getToken(key: String, value: String): String {
        return token.getString(key, value).toString()
    }

    fun setToken(key: String, value: String) {
        token.edit().putString(key, value).apply()
    }

    fun getEmail(key: String, value: String): String {
        return email.getString(key, value).toString()
    }

    fun setEmail(key: String, value: String) {
        email.edit().putString(key, value).apply()
    }
}