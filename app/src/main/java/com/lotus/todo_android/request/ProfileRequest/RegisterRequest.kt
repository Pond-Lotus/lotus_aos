package com.lotus.todo_android.request.ProfileRequest

import com.lotus.todo_android.Data.Profile.Register
import com.lotus.todo_android.response.ProfileResponse.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterRequest {
    @POST("/account/register/")
    fun requestRegister(
        @Body registerRequest: com.lotus.todo_android.Data.Profile.Register
    ) : Call<RegisterResponse>
}