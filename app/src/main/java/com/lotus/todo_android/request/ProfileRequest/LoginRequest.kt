package com.lotus.todo_android.request.ProfileRequest

import com.lotus.todo_android.Data.Profile.Login
import com.lotus.todo_android.response.ProfileResponse.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRequest {
    @POST("/account/login/")
    fun requestLogin(
        @Body loginRequest: com.lotus.todo_android.Data.Profile.Login
    ) : Call<LoginResponse>
}