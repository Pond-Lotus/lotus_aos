package com.example.todo_android.Request.ModifyRequest

import com.example.todo_android.Response.ModifyResponse.ChangeNicknameAndProfileResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ChangeNicknameAndProfileRequest {
    @Multipart
    @POST("/account/edit1/")
    fun requestChangeNicknameAndProfile(
        @Header("Authorization") token: String,
        @Header("imdel") imdel: Boolean,
        @Part ("nickname") nickname: String,
        @Part image : MultipartBody.Part
    ) : Call<ChangeNicknameAndProfileResponse>
}