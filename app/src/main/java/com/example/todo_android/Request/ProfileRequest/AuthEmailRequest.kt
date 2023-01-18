package com.example.todo_android.Request.ProfileRequest

import com.example.todo_android.Response.ProfileResponse.AuthEmailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthEmailRequest {
//    @FormUrlEncoded
    @GET("/account/emailcode/")
    fun requestEmail(
        @Query("email") email: String,
    ) : Call<AuthEmailResponse>
}

