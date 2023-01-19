package com.example.todo_android.Util

import android.content.Context
import android.content.SharedPreferences

class EmailUtil(context: Context) {

    val email: SharedPreferences = context.getSharedPreferences("email", Context.MODE_PRIVATE)


    fun getEmail(key: String, defValue: String): String {
        return email.getString(key, defValue).toString()
    }

    fun setEmail(key: String, str: String) {
        email.edit().putString(key, str).apply()
    }
}