package com.lotus.todo_android.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("prefs_name", 0)

    fun getData(key: String, value: String): String {
        return prefs.getString(key, value).toString()
    }

    fun setData(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}