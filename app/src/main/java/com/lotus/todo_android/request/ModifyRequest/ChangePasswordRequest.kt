package com.lotus.todo_android.request.ModifyRequest

import com.lotus.todo_android.Data.Modify.ChangePassword
import com.lotus.todo_android.response.ModifyResponse.ChangePasswordResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ChangePasswordRequest {
    @POST("/account/edit2/")
    fun requestChangePassword(
        @Header("Authorization") token: String,
        @Body changePasswordRequest: com.lotus.todo_android.Data.Modify.ChangePassword
    ): Call<ChangePasswordResponse>
}