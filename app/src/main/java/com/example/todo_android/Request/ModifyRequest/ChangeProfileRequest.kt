package com.example.todo_android.Request.ModifyRequest

import com.example.todo_android.Response.ModifyResponse.ChangeProfileResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ChangeProfileRequest {
    @Multipart
    @POST("/account/edit1/")
    fun requestChangeProfile(
        @Header("Authorization") token: String,
        @Part ("imdel") imdel: Boolean,
        @Part ("nickname") nickname: String,
        @Part image: MultipartBody.Part
    ) : Call<ChangeProfileResponse>
}