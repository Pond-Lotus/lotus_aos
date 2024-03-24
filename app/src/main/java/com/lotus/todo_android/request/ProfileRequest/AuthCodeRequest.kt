package com.lotus.todo_android.request.ProfileRequest

import com.lotus.todo_android.Data.Profile.AuthCode
import com.lotus.todo_android.response.ProfileResponse.AuthCodeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthCodeRequest {
    @POST("/account/emailcode/")
    fun requestCode(
        @Body authCodeRequest: com.lotus.todo_android.Data.Profile.AuthCode
    ) : Call<AuthCodeResponse>
}