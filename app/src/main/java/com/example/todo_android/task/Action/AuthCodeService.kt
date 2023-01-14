package com.example.todo_android.task.Action

import com.example.todo_android.Models.AuthCodeModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthCodeService {
//    @FormUrlEncoded
    @POST("/account/emailcode/")
    fun requestCode(
        @Body authCodeRequest: AuthCodeModel
    ) : Call<AuthCodeResponse>


//        fun requestCode(
//        @Field("email") email: String,
//        @Field("code") code: String
//        @FieldMap param: HashMap<String, Object>
//    ) : Call<AuthCode>
}