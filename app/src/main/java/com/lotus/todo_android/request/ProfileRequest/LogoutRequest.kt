package com.lotus.todo_android.request.ProfileRequest

import com.lotus.todo_android.response.ProfileResponse.LogoutResponse
import retrofit2.Call
import retrofit2.http.*

interface LogoutRequest {
    @POST("/account/logout/")
    fun requestLogout(
    @Header ("Authorization") token: String
    ) : Call<LogoutResponse>
}