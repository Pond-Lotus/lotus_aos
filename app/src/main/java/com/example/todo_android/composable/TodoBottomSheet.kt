package com.example.todo_android.composable


private fun convertToLayoutTimeFormat(time: String): String {
    return if (time == "미지정") {
        "미지정"
    } else if (time.length == 4) {
        "${time.substring(0, 2)}:${time.substring(2)}"
    } else {
        "${time.substring(0, 1)}:${time.substring(1)}"
    }
}