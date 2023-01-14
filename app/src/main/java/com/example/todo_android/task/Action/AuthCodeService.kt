package com.example.todo_android.task.Action

import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface AuthCodeService {
    @FormUrlEncoded
    @POST("/account/emailcode/")

    fun requestCode(
        @FieldMap param: HashMap<String, Object>
    ) : Call<AuthCode>


//        fun requestCode(
//        @Field("email") email: String,
//        @Field("code") code: String
//        @FieldMap param: HashMap<String, Object>
//    ) : Call<AuthCode>
}