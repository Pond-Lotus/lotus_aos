package com.example.todo_android.request.ModifyRequest

import com.example.todo_android.response.ModifyResponse.ChangeProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        @Part ("nickname") nickname: RequestBody,
        @Part image: MultipartBody.Part
    ) : Call<ChangeProfileResponse>
}