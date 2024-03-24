package com.lotus.todo_android.Data.Modify

import okhttp3.MultipartBody

data class ChangeProfile(
    val imdel: Boolean,
    val nickname: String,
    val image: MultipartBody.Part
)
