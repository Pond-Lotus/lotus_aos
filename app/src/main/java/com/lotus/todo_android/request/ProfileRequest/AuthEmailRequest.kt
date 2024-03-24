package com.lotus.todo_android.request.ProfileRequest

import com.lotus.todo_android.response.ProfileResponse.AuthEmailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthEmailRequest {
    @GET("/account/emailcode/")
    fun requestEmail(
        @Query("email") email: String
    ) : Call<AuthEmailResponse>
}

