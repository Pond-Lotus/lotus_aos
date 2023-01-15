package com.example.todo_android.task.Action

import com.example.todo_android.Data.AuthCode
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthCodeRequest {
//    @FormUrlEncoded
    @POST("/account/emailcode/")
    fun requestCode(
        @Body authCodeRequest: AuthCode
    ) : Call<AuthCodeResponse>


//        fun requestCode(
//        @Field("email") email: String,
//        @Field("code") code: String
//        @FieldMap param: HashMap<String, Object>
//    ) : Call<AuthCode>
}