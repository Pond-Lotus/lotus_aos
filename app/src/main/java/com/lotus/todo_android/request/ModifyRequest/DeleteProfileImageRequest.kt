package com.lotus.todo_android.request.ModifyRequest

import com.lotus.todo_android.response.ModifyResponse.DeleteProfileImageResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface DeleteProfileImageRequest {
    @Multipart
    @POST("/account/edit1/")
    fun requestDeleteProfileImage(
        @Header("Authorization") token: String,
        @Part("nickname") nickname: RequestBody,
        @Part("imdel") imdel: Boolean
    ): Call<DeleteProfileImageResponse>
}