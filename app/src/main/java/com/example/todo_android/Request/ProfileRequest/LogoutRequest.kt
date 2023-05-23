package com.example.todo_android.Request.ProfileRequest

import com.example.todo_android.Data.Profile.Logout
import com.example.todo_android.Response.ProfileResponse.LogoutResponse
import retrofit2.Call
import retrofit2.http.*

interface LogoutRequest {
    @POST("/account/logout/")
    fun requestLogout(
    @Header ("Authorization") token: String
    ) : Call<LogoutResponse>
}