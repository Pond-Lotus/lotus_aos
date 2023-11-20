package com.example.todo_android.request.ProfileRequest

import com.example.todo_android.response.ProfileResponse.DeleteAccountResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Header

interface DeleteAccountRequest {
    @DELETE("/account/withdrawal/")
    fun requestDeleteAccount(
        @Header("Authorization") token: String
    ): Call<DeleteAccountResponse>
}