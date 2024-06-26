package com.lotus.todo_android.request.ProfileRequest

import com.lotus.todo_android.response.ProfileResponse.SearchEmailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FindOutPassWordRequest {
    @GET("/account/findpw/")
    fun requestFindOutPassWord(
        @Query("email") email: String
    ) : Call<SearchEmailResponse>
}