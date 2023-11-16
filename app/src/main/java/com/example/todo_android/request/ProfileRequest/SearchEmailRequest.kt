package com.example.todo_android.request.ProfileRequest

import com.example.todo_android.response.ProfileResponse.SearchEmailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchEmailRequest {
    @GET("/account/findpw/")
    fun requestEmail(
        @Query("email") email: String
    ) : Call<SearchEmailResponse>
}